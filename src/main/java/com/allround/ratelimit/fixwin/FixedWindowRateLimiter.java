package com.allround.ratelimit.fixwin;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口算法
 * 原理：在固定的时间窗口内统计请求的数量，如果超过阈值，则拒绝后续请求。
 * 优点：实现简单。
 * 缺点：存在边缘效应，即在窗口切换时可能出现短暂的过载。
 *
 *
 * 计数器重置:
 * resetCounters 方法重置所有客户端的计数器。
 * scheduleReset 方法启动了一个定时任务，每隔 windowSizeMillis 毫秒重置计数器。
 * 允许请求:
 * allow 方法尝试为指定的客户端增加一个请求。
 * 如果增加了请求后超过了 maxRequests，则不允许新的请求，并返回 false。
 * 如果请求被允许，则返回 true。
 * 关闭定时任务:
 * shutdown 方法关闭了 ScheduledExecutorService，停止计数器的重置。
 * 主函数:
 * 在 main 方法中，我们创建了一个 FixedWindowRateLimiter 实例，并模拟了多个请求的产生。
 * 每次尝试增加请求时，根据是否成功增加打印相应的消息。
 */
public class FixedWindowRateLimiter {

    /**
     * 初始化:
     *  counters 是一个 ConcurrentHashMap，用于存储每个客户端的请求计数。
     *  maxRequests 表示每个窗口的最大请求数。
     *  windowSizeMillis 表示每个窗口的大小，单位为毫秒。
     *  scheduler 是一个 ScheduledExecutorService，用于周期性地清空计数器。
     */
    private final Map<String, AtomicInteger> counters;
    private final int maxRequests;
    private final long windowSizeMillis;
    private final ScheduledExecutorService scheduler;

    public FixedWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.counters = new ConcurrentHashMap<>();
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        resetCounters();
        scheduleReset();
    }

    private void resetCounters() {
        counters.replaceAll((k, v) -> new AtomicInteger(0));
    }

    private void scheduleReset() {
        scheduler.scheduleAtFixedRate(this::resetCounters, windowSizeMillis, windowSizeMillis, TimeUnit.MILLISECONDS);
    }

    public boolean allow(String clientId) {
        AtomicInteger counter = counters.computeIfAbsent(clientId, k -> new AtomicInteger());
        if (counter.incrementAndGet() > maxRequests) {
            // 如果超过了最大请求数量，则不允许新的请求
            return false;
        }
        return true;
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public static void test() throws InterruptedException {
        int maxRequests = 10; // 每个窗口的最大请求数
        long windowSizeMillis = 5000; // 窗口大小，单位为毫秒

        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(maxRequests, windowSizeMillis);

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
