package flink.learning.example.word_count_stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.guava18.com.google.common.collect.ImmutableMap;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkFixedPartitioner;
import org.apache.flink.util.Preconditions;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class WordCountStream {
    private final String baseFilePath;
    private final String randomSeed;
    private final String inputTopic;
    private final String outputTopic;
    private final String bootstrapServers;
    private final MultipleParameterTool parameters;

    public WordCountStream(
            String baseFilePath,
            String randomSeed,
            String inputTopic,
            String outputTopic,
            String bootstrapServers,
            MultipleParameterTool parameters
    ) {
        this.baseFilePath = baseFilePath;
        this.randomSeed = randomSeed;
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.bootstrapServers = bootstrapServers;
        this.parameters = parameters;
    }

    public void run() throws Exception {
        final StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.getConfig().setGlobalJobParameters(parameters);
        Properties kafkaParameters = ParameterTool.fromMap(ImmutableMap.of(
                "bootstrap.servers", bootstrapServers,
                "group.id", String.format("%s_group", WordCountStream.class.getSimpleName())
        )).getProperties();
        generateMessageToKafka(
                executionEnvironment,
                inputTopic,
                kafkaParameters,
                new RandomWordGenerator(
                        baseFilePath,
                        randomSeed,
                        500,
                        TimeUnit.MILLISECONDS
                )
        );
        wordCount(
                executionEnvironment,
                inputTopic,
                outputTopic,
                kafkaParameters
        );
        executionEnvironment.addSource(new FlinkKafkaConsumer<>(
                outputTopic,
                new SimpleStringSchema(),
                kafkaParameters
        )).print();
        executionEnvironment.execute(WordCountStream.class.getSimpleName());
    }

    private void wordCount(
            StreamExecutionEnvironment executionEnvironment,
            String inputTopic,
            String outputTopic,
            Properties kafkaProperties
    ) {
        SourceFunction<String> sourceFunction = new FlinkKafkaConsumer<>(
                inputTopic,
                new SimpleStringSchema(),
                kafkaProperties
        );
        SinkFunction<String> sinkFunction = new FlinkKafkaProducer<>(
                outputTopic,
                new SimpleStringSchema(),
                kafkaProperties,
                new FlinkFixedPartitioner<>(),
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE,
                FlinkKafkaProducer.DEFAULT_KAFKA_PRODUCERS_POOL_SIZE
        );
        executionEnvironment.addSource(sourceFunction)
                .flatMap(new Tokenizer())
                .keyBy((KeySelector<Tuple2<String, Integer>, Object>) value -> value.f0)
                .sum(1)
                .map((MapFunction<Tuple2<String, Integer>, String>) value
                        -> String.format("word(%s) => count(%s)", value.f0, value.f1))
                .addSink(sinkFunction);
    }

    private void generateMessageToKafka(
            StreamExecutionEnvironment env,
            String inputTopic,
            Properties kafkaProperties,
            RandomWordGenerator randomWordGenerator
    ) {
        SourceFunction<String> sourceFunction = new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> context) throws Exception {
                if (!randomWordGenerator.configured()) {
                    randomWordGenerator.configure();
                }
                StreamSupport.stream(randomWordGenerator.spliterator(), false)
                        .forEach(context::collect);
            }

            @Override
            public void cancel() {
                randomWordGenerator.ends();
            }
        };
        SinkFunction<String> sinkFunction = new FlinkKafkaProducer<>(
                inputTopic,
                new SimpleStringSchema(),
                kafkaProperties,
                new FlinkFixedPartitioner<>(),
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE,
                FlinkKafkaProducer.DEFAULT_KAFKA_PRODUCERS_POOL_SIZE
        );
        env.addSource(sourceFunction)
                .addSink(sinkFunction);
    }

    public static void main(String[] args) throws Exception {
        final MultipleParameterTool parameters = MultipleParameterTool.fromArgs(args);
        String baseFilePath = parameters.get("baseFilePath", "word_count_stream/data/the_little_match_girl.txt");
        Preconditions.checkNotNull(baseFilePath, "baseFilePath(%s) cannot be null");
        String randomSeed = parameters.get("randomSeed", "flink.word_count_stream.random_seed");
        String inputTopic = parameters.get("input_topic", "flink_learning.word_count_batch.input");
        String outputTopic = parameters.get("output_topic", "flink_learning.word_count_batch.output");
        String bootstrapServers = parameters.get("bootstrap_servers", "localhost:9092");

        WordCountStream wordCountStream = new WordCountStream(
                baseFilePath,
                randomSeed,
                inputTopic,
                outputTopic,
                bootstrapServers,
                parameters
        );
        wordCountStream.run();
    }
}
