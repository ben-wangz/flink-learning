package flink.learning.example.meteorological_data.table;

import flink.learning.example.meteorological_data.MeteorologicalData;
import flink.learning.example.meteorological_data.MeteorologicalDataStream;
import flink.learning.example.meteorological_data.MeteorologicalSink;
import flink.learning.example.meteorological_data.MeteorologicalSource;
import org.apache.flink.calcite.shaded.com.google.common.collect.ImmutableList;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.concurrent.TimeUnit;

import static org.apache.flink.table.api.Expressions.$;

public class StreamJob {
    public static void main(String[] args) throws Exception {
        String randomSeed = "randomSeed";
        String serviceHost = "localhost";
        int servicePort = 8080;
        StreamExecutionEnvironment bsEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment bsTableEnv = StreamTableEnvironment.create(bsEnv, bsSettings);

        SourceFunction<String> sourceFunction = new MeteorologicalSource(
                randomSeed,
                1000,
                TimeUnit.MILLISECONDS,
                ImmutableList.of("1", "2", "3")
        );
        DataStreamSource<String> sourceStream = bsEnv.addSource(sourceFunction);
        Table sourceTable = bsTableEnv.fromDataStream(sourceStream, $("message"));
        bsTableEnv.createTemporaryView("t_source", sourceTable);
        Table extractedTable = bsTableEnv.sqlQuery("select " +
                "SPLIT_INDEX(message, ',', 0) as eventTime, " +
                "SPLIT_INDEX(message, ',', 1) as id, " +
                "SPLIT_INDEX(message, ',', 2) as temperature " +
                "from t_source"
        );
        bsTableEnv.createTemporaryView("t_extracted", extractedTable);
        Table filteredTable = bsTableEnv.sqlQuery(
                "select eventTime as `key`, temperature as `value` from t_extracted where id = '3' or id = '1'"
        );
        SinkFunction<MeteorologicalData> sinkFunction = new MeteorologicalSink<>(
                serviceHost,
                servicePort,
                MeteorologicalDataStream.class.getSimpleName());
        DataStream<MeteorologicalData> streamToSink = bsTableEnv.toAppendStream(filteredTable, MeteorologicalData.class);
        streamToSink.addSink(sinkFunction);
//        streamToSink.print();
        bsEnv.execute();
    }
}
