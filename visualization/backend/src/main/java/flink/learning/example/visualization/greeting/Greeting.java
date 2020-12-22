package flink.learning.example.visualization.greeting;

import flink.learning.example.visualization.core.Configurable;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class Greeting implements Configurable<String> {
    private final String username;

    public Greeting(String username) {
        this.username = username;
    }

    @Override
    @PostConstruct
    public void open() throws IOException {
        System.out.println("opening...");
    }

    @Override
    public void close() throws IOException {
        System.out.println("closing...");
    }

    @Override
    public String call() {
        return String.format("hello %s", username);
    }
}
