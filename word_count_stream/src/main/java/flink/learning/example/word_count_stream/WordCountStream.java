package flink.learning.example.word_count_stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.util.Collector;
import org.apache.flink.util.Preconditions;

public class WordCountStream {
    public static void main(String[] args) throws Exception {
        final MultipleParameterTool params = MultipleParameterTool.fromArgs(args);
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params);

        String input = params.get("input");
        String output = params.get("output");
        Preconditions.checkNotNull(input, "input cannot be null");
        Preconditions.checkNotNull(output, "output cannot be null");
        DataSet<String> text = env.readTextFile(input);
        DataSet<Tuple2<String, Integer>> counts = text.flatMap(new Tokenizer())
                .groupBy(0)
                .sum(1);
        counts.writeAsFormattedText(
                output,
                (TextOutputFormat.TextFormatter<Tuple2<String, Integer>>) value
                        -> String.format("word(%s) => count(%s)", value.f0, value.f1)
        );
        env.execute(WordCountStream.class.getSimpleName());
    }

    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            String[] tokens = value.toLowerCase().split("\\W+");
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }
}
