package flink.learning.example.meteorological_data;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;

public class MeteorologicalDataStream {
    private final String randomSeed;
    private final String serviceHost;
    private final int servicePort;
    private final MultipleParameterTool parameters;

    public MeteorologicalDataStream(
            String randomSeed,
            String serviceHost,
            int servicePort,
            MultipleParameterTool parameters
    ) {
        this.randomSeed = randomSeed;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.parameters = parameters;
    }

    public void run() throws Exception {
        final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().setGlobalJobParameters(parameters);
        SourceFunction<String> sourceFunction = new MeteorologicalSource(randomSeed, 1000, TimeUnit.MILLISECONDS);
        SinkFunction<MeteorologicalData> sinkFunction = new MeteorologicalSink<>(
                serviceHost,
                servicePort,
                MeteorologicalDataStream.class.getSimpleName());
        executionEnvironment.addSource(sourceFunction)
                .map((MapFunction<String, MeteorologicalData>) value -> new MeteorologicalData(
                        String.valueOf(System.currentTimeMillis()), value))
                .addSink(sinkFunction);
        executionEnvironment.execute(MeteorologicalDataStream.class.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        final MultipleParameterTool parameters = MultipleParameterTool.fromArgs(args);
        String randomSeed = parameters.get("randomSeed", "flink.meteorological_data_stream.random_seed");
        String serviceHost = parameters.get("serviceHost", "localhost");
        int servicePort = parameters.getInt("servicePort", 8080);
        MeteorologicalDataStream meteorologicalDataStream = new MeteorologicalDataStream(
                randomSeed,
                serviceHost,
                servicePort,
                parameters
        );
        meteorologicalDataStream.run();
    }
}
