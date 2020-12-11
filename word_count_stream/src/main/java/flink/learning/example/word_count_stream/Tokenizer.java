package flink.learning.example.word_count_stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.guava18.com.google.common.base.Splitter;
import org.apache.flink.util.Collector;

import java.util.stream.StreamSupport;

public class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
    private static final Splitter SPLITTER = Splitter.onPattern("\\W+").omitEmptyStrings();

    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
        StreamSupport.stream(SPLITTER.split(value).spliterator(), false)
                .map(word -> new Tuple2<>(word, 1))
                .forEach(out::collect);
    }
}
