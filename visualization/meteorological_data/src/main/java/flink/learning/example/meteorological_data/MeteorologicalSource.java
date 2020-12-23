package flink.learning.example.meteorological_data;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class MeteorologicalSource implements SourceFunction<String> {
    private final RandomMeteorologicalDataGenerator randomMeteorologicalDataGenerator;

    public MeteorologicalSource(
            String randomSeed,
            int sleep,
            TimeUnit timeUnit) {
        randomMeteorologicalDataGenerator = new RandomMeteorologicalDataGenerator(
                randomSeed,
                sleep,
                timeUnit
        );
    }

    @Override
    public void run(SourceContext<String> context) throws Exception {
        if (!randomMeteorologicalDataGenerator.configured()) {
            randomMeteorologicalDataGenerator.configure();
        }
        StreamSupport.stream(randomMeteorologicalDataGenerator.spliterator(), false)
                .forEach(context::collect);
    }

    @Override
    public void cancel() {
        randomMeteorologicalDataGenerator.ends();
    }
}
