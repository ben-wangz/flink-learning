package flink.learning.example.visualization.service;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataKeeperService {
    private final Map<String, String> dataPool;

    public DataKeeperService() {
        this.dataPool = new HashMap<>();
    }

    public void updateData(String name, String json) {
        dataPool.put(name, json);
    }

    public Map<String, String> queryDataWithPrefix(String namePrefix, int limit) {
        return dataPool.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(namePrefix))
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replace(namePrefix, ""),
                        Map.Entry::getValue
                ));
    }

    public void clear() {
        dataPool.clear();
    }
}
