package flink.learning.example.meteorological_data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RandomMeteorologicalDataGenerator implements Iterable<String>, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomMeteorologicalDataGenerator.class);
    private final String randomSeed;
    private final int sleep;
    private final TimeUnit timeUnit;
    private transient Random random;
    private transient boolean ends;
    private transient Double lastTemperature;

    public RandomMeteorologicalDataGenerator(
            String randomSeed,
            int sleep,
            TimeUnit timeUnit
    ) {
        this.randomSeed = randomSeed;
        this.sleep = sleep;
        this.timeUnit = timeUnit;
    }

    public boolean configured() {
        return null != lastTemperature && null != random;
    }

    public void configure() throws IOException {
        random = new Random(randomSeed.hashCode());
        ends = false;
        lastTemperature = 20.0;
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
                double wave = random.nextDouble() * 3 - 3.0 / 2;
                double currentTemperature = lastTemperature + wave;
                lastTemperature = currentTemperature;
                return String.valueOf(currentTemperature);
            }
        };
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
