package flink.learning.example.meteorological_data;

import java.io.Serializable;

public interface KeyValueData extends Serializable {
    String key();

    String value();
}
