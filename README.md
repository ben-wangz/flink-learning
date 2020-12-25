### flink-learning

1. 先来看看 [flink 是什么](doc/introduction.md)
2. 然后，我们从 hello world 起步: [word_count](doc/word_count.md)
3. 认识一下 batch 和 stream: [batch_vs_stream](doc/batch_vs_stream.md)
4. flink 系统的框架: [flink_architecture](doc/flink_architecture.md)
5. state 概念篇
6. time 概念篇
7. window 概念篇
8. 从场景中寻找 flink 的强大
    * 批处理: [word_count_batch](doc/word_count_batch.md)
    * 流处理: [word_count_stream](doc/word_count_stream.md)
    * 气象数据实时可视化的例子: [visualization:integration](doc/visualization.md)
    * Operators 与场景
        + map/flatMap/filter
        + sum/min/max
        + keyBy/minBy/maxBy
        + reduce
        + union
        + join/cross/coGroup
    * physical partitioning
        + partitionCustom
        + shuffle
        + rebalance
        + rescale
        + broadcast
    * window(stream only)
        + window
        + windowAll
        + apply
        + window reduce
        + window aggregation
        + window join
        + interval join
        + coGroup
    * chaining(stream only)
        + startNewChain
        + disableChaining
        + slotSharingGroup
    * others
        + fold
        + coMap/coFlatMap
        + ...
9. 启动源码解读
10. source
    * 自定义一个 source
    * 源码解读
11. sink
    * 自定义一个 sink
    * 源码解读
12. checkpoint 与 savepoint
    * 概念篇
    * 如何启用
    * 源码解读
13. 迭代
14. flink sql 接口
15. udf/udaf/udtf
16. 最佳实践: 实时气象数据分析
