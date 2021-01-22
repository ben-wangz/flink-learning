package flink.learning.example.code_trace_life_cycle;

import org.apache.flink.shaded.guava18.com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RandomWordGenerator implements Iterable<String>, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomWordGenerator.class);
    private static final Splitter SPLITTER = Splitter.onPattern("\\W+").omitEmptyStrings();
    private final String baseDataPath;
    private final String randomSeed;
    private final int sleep;
    private final TimeUnit timeUnit;
    private transient List<String> words;
    private transient Random random;
    private transient boolean ends;

    public RandomWordGenerator(
            String baseDataPath,
            String randomSeed,
            int sleep,
            TimeUnit timeUnit
    ) {
        this.baseDataPath = baseDataPath;
        this.randomSeed = randomSeed;
        this.sleep = sleep;
        this.timeUnit = timeUnit;
    }

    public boolean configured() {
        return null != words && null != random;
    }

    public void configure() throws IOException {
        try (Stream<String> stringStream = Files.lines(Paths.get(baseDataPath))) {
            words = stringStream
                    .flatMap(line -> StreamSupport.stream(SPLITTER.split(line).spliterator(), false))
                    .distinct()
                    .collect(Collectors.toList());
        }
        random = new Random(randomSeed.hashCode());
        ends = false;
    }

    public void ends() {
        LOGGER.info("ending the generator");
        ends = true;
    }

    @Nonnull
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return !ends;
            }

            @Override
            public String next() {
                try {
                    timeUnit.sleep(sleep);
                } catch (InterruptedException e) {
                    LOGGER.warn(String.format("interrupted: %s", e.getMessage()), e);
                }
                int randomLength = random.nextInt(20) + 1;
                return IntStream.range(0, randomLength)
                        .mapToObj(index -> words.get(random.nextInt(words.size())))
                        .collect(Collectors.joining(" "));
            }
        };
    }

    public String getBaseDataPath() {
        return baseDataPath;
    }

    public String getRandomSeed() {
        return randomSeed;
    }

    public int getSleep() {
        return sleep;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
