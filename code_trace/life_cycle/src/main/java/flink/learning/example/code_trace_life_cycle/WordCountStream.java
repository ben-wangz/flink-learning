package flink.learning.example.code_trace_life_cycle;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Preconditions;

import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class WordCountStream {
    private final String baseFilePath;
    private final String randomSeed;
    private final MultipleParameterTool parameters;

    public WordCountStream(
            String baseFilePath,
            String randomSeed,
            MultipleParameterTool parameters
    ) {
        this.baseFilePath = baseFilePath;
        this.randomSeed = randomSeed;
        this.parameters = parameters;
    }

    public void run() throws Exception {
        final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().setGlobalJobParameters(parameters);
        RandomWordGenerator randomWordGenerator = new RandomWordGenerator(
                baseFilePath,
                randomSeed,
                500,
                TimeUnit.MILLISECONDS
        );
        SourceFunction<String> sourceFunction = new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> context) throws Exception {
                if (!randomWordGenerator.configured()) {
                    randomWordGenerator.configure();
                }
                StreamSupport.stream(randomWordGenerator.spliterator(), false)
                        .forEach(context::collect);
            }

            @Override
            public void cancel() {
                randomWordGenerator.ends();
            }
        };
        executionEnvironment.addSource(sourceFunction)
                .flatMap(new Tokenizer())
                .keyBy((KeySelector<Tuple2<String, Integer>, Object>) value -> value.f0)
                .sum(1)
                .map((MapFunction<Tuple2<String, Integer>, String>) value
                        -> String.format("word(%s) => count(%s)", value.f0, value.f1))
                .print();
        executionEnvironment.execute(WordCountStream.class.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        final MultipleParameterTool parameters = MultipleParameterTool.fromArgs(args);
        String baseFilePath = parameters.get("baseFilePath", "word_count_stream/data/the_little_match_girl.txt");
        Preconditions.checkNotNull(baseFilePath, "baseFilePath(%s) cannot be null");
        String randomSeed = parameters.get("randomSeed", "flink.word_count_stream.random_seed");

        WordCountStream wordCountStream = new WordCountStream(
                baseFilePath,
                randomSeed,
                parameters
        );
        wordCountStream.run();
    }
}
