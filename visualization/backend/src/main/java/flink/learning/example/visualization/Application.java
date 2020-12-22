package flink.learning.example.visualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public void run(String... args) throws IOException {
        LOGGER.info("running with arguments: {}", String.join(" ", args));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
