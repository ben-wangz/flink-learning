package flink.learning.example.visualization.core;

import java.io.Closeable;
import java.io.IOException;

public interface Configurable<ReturnType> extends Closeable {
    void open() throws IOException;

    void close() throws IOException;

    ReturnType call();
}
