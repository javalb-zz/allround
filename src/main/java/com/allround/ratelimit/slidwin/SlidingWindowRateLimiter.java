package com.allround.ratelimit.slidwin;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口算法
 * 原理：滑动窗口算法也是统计一段时间内的请求数量，但它通过维护一个滑动窗口来平滑统计，每个时间段内可以单独计数，这样可以更准确地反映实时负载。
 * 优点：比固定窗口算法更精确，能够更好地处理突发流量。
 * 缺点：实现相对复杂，需要额外的空间来存储每个时间窗口的数据
 *
 * 窗口更新:
 * updateWindow 方法遍历每个客户端的请求计数队列，移除已经过期的时间窗口。
 * scheduleUpdate 方法启动了一个定时任务，每隔 windowTickMillis 毫秒更新窗口。
 * 允许请求:
 * allow 方法尝试为指定的客户端增加一个请求。
 * 如果增加了请求后不超过 maxRequests，则允许新的请求，并返回 true。
 * 如果请求被允许，则将当前时间加入队列。
 * 如果请求超过了限制，则返回 false。
 * 关闭定时任务:
 * shutdown 方法关闭了 ScheduledExecutorService，停止窗口的更新。
 * 主函数:
 * 在 main 方法中，我们创建了一个 SlidingWindowRateLimiter 实例，并模拟了多个请求的产生。
 * 每次尝试增加请求时，根据是否成功增加打印相应的消息。
 */
public class SlidingWindowRateLimiter {

    /**
     * 初始化:
     *  * counters 是一个 ConcurrentHashMap，用于存储每个客户端的请求计数队列。
     *  * maxRequests 表示每个窗口的最大请求数。
     *  * windowSizeMillis 表示每个窗口的大小，单位为毫秒。
     *  * windowTickMillis 表示窗口更新的频率，单位为毫秒。
     *  * scheduler 是一个 ScheduledExecutorService，用于周期性地更新窗口。
     */
    private final Map<String, Queue<AtomicInteger>> counters;
    private final int maxRequests;
    private final long windowSizeMillis;
    private final long windowTickMillis;
    private final ScheduledExecutorService scheduler;

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMillis, long windowTickMillis) {
        this.counters = new ConcurrentHashMap<>();
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.windowTickMillis = windowTickMillis;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduleUpdate();
    }

    private void scheduleUpdate() {
        scheduler.scheduleAtFixedRate(this::updateWindow, windowTickMillis, windowTickMillis, TimeUnit.MILLISECONDS);
    }

    private void updateWindow() {
        long currentTime = System.currentTimeMillis();
        counters.forEach((clientId, queue) -> {
            while (!queue.isEmpty() && currentTime - queue.peek().get() > windowSizeMillis) {
                queue.poll();
            }
        });
    }

    public boolean allow(String clientId) {
        Queue<AtomicInteger> queue = counters.computeIfAbsent(clientId, k -> new ConcurrentLinkedQueue<>());
        AtomicInteger counter = new AtomicInteger(queue.size());

        if (queue.size() == 0 || counter.get() <= maxRequests) {
            queue.offer(new AtomicInteger(currentTime().intValue()));
            return true;
        }

        return false;
    }

    private Long currentTime() {
        return System.currentTimeMillis();
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public static void test() throws InterruptedException {
        int maxRequests = 10; // 每个窗口的最大请求数
        long windowSizeMillis = 5000; // 窗口大小，单位为毫秒
        long windowTickMillis = 1000; // 窗口更新的频率，单位为毫秒

        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(maxRequests, windowSizeMillis, windowTickMillis);

        // 模拟产生请求
        for (int i = 0; i < 30; i++) {
            String clientId = "client-" + (i % 3); // 模拟三个客户端
            if (rateLimiter.allow(clientId)) {
                System.out.println("Request " + i + " from " + clientId + " processed.");
            } else {
                System.out.println("Request " + i + " from " + clientId + " dropped.");
            }
            Thread.sleep(200); // 控制请求产生的速率
        }

        rateLimiter.shutdown();
    }
}
