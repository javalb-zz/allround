package com.allround.snowflake;
import java.util.concurrent.atomic.AtomicLong;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * 优点
 * 唯一性：雪花算法可以生成全局唯一的ID，每个ID都是独一无二的，不会重复。
 * 高性能：雪花算法生成ID的速度非常快，可以在短时间内生成大量的ID。
 * 可排序：雪花算法生成的ID是按照时间顺序递增的，可以根据ID的大小来判断生成的时间先后顺序。
 * 分布式：雪花算法可以在分布式系统中使用，不同的节点可以独立生成ID，不会产生冲突。
 *
 * 缺点
 * 依赖系统时钟：雪花算法的唯一性依赖于系统时钟的准确性，如果系统时钟发生回拨或者不同步，可能会导致生成的ID重复。
 * 时钟回拨问题：如果系统时钟发生回拨，可能会导致生成的ID比之前生成的ID小，这会破坏ID的递增顺序。
 * 时钟同步问题：在分布式系统中，不同节点的系统时钟可能存在不同步的情况，这可能会导致生成的ID不是全局唯一的。
 * 有限的并发性：雪花算法中的每个部分（时间戳、机器ID、序列号）都有一定的位数限制，这限制了并发生成ID的数量。
 *
 * 其实雪花算法每一部分占用的比特位数量并不是固定死的。例如你的业务可能达不到 69 年之久，那么可用减少时间戳占用的位数，雪花算法服务需要部署的节点超过1024 台，那么可将减少的位数补充给机器码用。
 * 注意，雪花算法中 41 位比特位不是直接用来存储当前服务器毫秒时间戳的，而是需要当前服务器时间戳减去某一个初始时间戳值，一般可以使用服务上线时间作为初始时间戳值。
 * 对于机器码，可根据自身情况做调整，例如机房号，服务器号，业务号，机器 IP 等都是可使用的。对于部署的不同雪花算法服务中，最后计算出来的机器码能区分开来即可。
 */
public class SnowflakeIDGenerator {
    // 起始的时间戳
    private final static long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00

    // 每部分占用的位数
    private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final static long WORKER_BIT = 10; // 工作机器ID占用的位数
    private final static long TIMESTAMP_BIT = 41; // 时间戳占用的位数

    // 每部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_BIT);

    // 每部分向左的位移
    private final static long WORKER_LEFT = SEQUENCE_BIT;
    private final static long TIMESTAMP_LEFT = SEQUENCE_BIT + WORKER_BIT;

    private long workerId; // 工作机器ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上次生成ID的时间戳

    public SnowflakeIDGenerator(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (workerId << WORKER_LEFT)
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}