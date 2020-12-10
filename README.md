### flink-learning

1. 先来看看 [flink 是什么](doc/introduction.md)
2. 然后，我们从 hello world 起步: [word_count](doc/word_count.md)
3. 认识一下 batch 和 stream: [batch_vs_stream](doc/batch_vs_stream.md)
4. flink 系统的框架: [flink_architecture](doc/flink_architecture.md)
5. state 概念篇
6. time 概念篇
7. 从场景中寻找 flink 的强大
    * 批处理: [word_count_batch](doc/word_count_batch.md)
    * 流处理: [word_count_stream](doc/word_count_stream.md)
    * Operators
        + map
        + flatMap
        + filter
        + keyBy/minBy/maxBy
        + reduce
        + fold
        + sum/min/max
        + window/windowAll/WindowFunction
        + window ReduceFunction/window FoldFunction/window Aggregations
        + union
        + join
        + intervalJoin
        + coGroup
        + connect
        + CoMapFunction
        + split/select
        + IterativeStream
    * physical partitioning
        + partitionCustom
        + shuffle
        + rebalance
        + rescale
        + broadcast
    * chaining
        + startNewChain
        + disableChaining
        + slotSharingGroup
8. 启动源码解读
9. 自定义 source
10. source 源码解读
11. 自定义 sink
12. sink 源码解读
13. checkpoint 源码解读
14. flink sql 接口
15. udf/udaf/udtf
16. 最佳实践: 实时气象数据分析
