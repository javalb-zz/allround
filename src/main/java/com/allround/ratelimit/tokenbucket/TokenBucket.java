package com.allround.ratelimit.tokenbucket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 令牌桶算法
 * 原理：令牌桶算法同样使用一个桶来存储请求，但是桶中存储的是令牌。桶以恒定的速率向桶中添加令牌，当请求到来时，需要消耗一个令牌才能继续处理。如果桶中没有足够的令牌，则请求被拒绝。
 * 优点：能够处理突发流量，更加灵活。
 * 缺点：实现稍微复杂一些。
 */
import java.util.concurrent.*;

public class TokenBucket {

    /**
     * capacity 表示桶的最大容量。
     * tokens 是一个 AtomicInteger，用于原子性地管理桶中的令牌数量。
     * refillRate 表示每多少毫秒向桶中添加一个令牌。
     * scheduler 是一个 ScheduledExecutorService，用于周期性地向桶中添加令牌
     */
    private final int capacity;
    private final AtomicInteger tokens;
    private final long refillRate;
    private final ScheduledExecutorService scheduler;

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.tokens = new AtomicInteger(capacity);
        this.refillRate = refillRate;
        this.scheduler = Executors.newScheduledThreadPool(1);
        startRefilling();
    }

    private void startRefilling() {
        scheduler.scheduleAtFixedRate(() -> {
            int currentTokens = tokens.get();
            if (currentTokens < capacity) {
                // 如果桶没有满，则添加令牌
                tokens.incrementAndGet();
            }
        }, 0, refillRate, TimeUnit.MILLISECONDS);
    }

    public boolean tryConsume() {
        while (true) {
            int currentTokens = tokens.get();
            if (currentTokens == 0) {
                // 没有足够的令牌，等待下一个令牌的到来
                try {
                    TimeUnit.MILLISECONDS.sleep(refillRate);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } else if (tokens.compareAndSet(currentTokens, currentTokens - 1)) {
                // 成功消费一个令牌
                return true;
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public static void test() throws InterruptedException {
        int capacity = 10; // 桶的最大容量
        int refillRate = 1000; // 每秒添加令牌的数量，单位为毫秒

        TokenBucket tokenBucket = new TokenBucket(capacity, refillRate);

        // 模拟产生请求
        for (int i = 0; i < 20; i++) {
            if (tokenBucket.tryConsume()) {
                System.out.println("Request " + i + " processed.");
            } else {
                System.out.println("Request " + i + " dropped.");
            }
            Thread.sleep(200); // 控制请求产生的速率
        }

        tokenBucket.shutdown();
    }
}
