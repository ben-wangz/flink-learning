package flink.learning.example.meteorological_data;

import flink.learning.example.meteorological_data.table.StreamJob;

public class Entrypoint {
    public static void main(String[] args) throws Exception {
        if (0 == args.length) {
            regularJob(args);
            return;
        }
        if ("regular".equals(args[0])) {
            regularJob(args);
        } else if ("table".equals(args[0])) {
            tableJob(args);
        } else {
            throw new RuntimeException("invalid arguments");
        }
    }

    private static void regularJob(String[] args) throws Exception {
        MeteorologicalDataStream.main(args);
    }

    private static void tableJob(String[] args) throws Exception {
        StreamJob.main(args);
    }
}
