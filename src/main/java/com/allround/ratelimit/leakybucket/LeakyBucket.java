package com.allround.ratelimit.leakybucket;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 漏桶算法
 * 原理：漏桶算法使用一个固定容量的桶来存储请求，桶以恒定的速率向外漏出请求。如果桶满了，那么新的请求会被拒绝。
 * 优点：简单易实现，易于理解和维护。
 * 缺点：无法应对突发流量。
 */
public class LeakyBucket {

    private final LinkedBlockingQueue<Packet> bucket;
    private final ScheduledExecutorService scheduler;
    private final AtomicInteger currentRate;
    private final int maxCapacity;
    private final int leakRate;

    public LeakyBucket(int maxCapacity, int leakRate) {
        this.bucket = new LinkedBlockingQueue<>(maxCapacity);
        this.currentRate = new AtomicInteger(0);
        this.maxCapacity = maxCapacity;
        this.leakRate = leakRate;
        this.scheduler = Executors.newScheduledThreadPool(1);

        // 开始漏桶的流出过程
        startLeaking();
    }

    /**
     * 尝试添加一个数据包到桶中。
     * 如果桶满了，则丢弃数据包。
     */
    public boolean addPacket(Packet packet) {
        try {
            if (bucket.offer(packet)) {
                return true; // 数据包成功放入桶中
            } else {
                return false; // 桶满了，数据包被丢弃
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 模拟漏桶的流出过程。
     */
    private void startLeaking() {
        scheduler.scheduleAtFixedRate(() -> {
            Packet packet;
            while ((packet = bucket.poll()) != null) {
                // 漏出一个数据包
                currentRate.incrementAndGet();
                System.out.println("Packet leaked: " + packet.getData());
            }
        }, 0, 1000 / leakRate, TimeUnit.MILLISECONDS); // 每秒漏出 leakRate 个数据包
    }

    /**
     * 获取当前的漏出速率。
     */
    public int getCurrentRate() {
        return currentRate.get();
    }

    public static void test() {
        int maxCapacity = 10; // 桶的最大容量
        int leakRate = 1; // 每秒漏出的数据包数量

        LeakyBucket leakyBucket = new LeakyBucket(maxCapacity, leakRate);
        // 模拟数据包的产生
        for (int i = 0; i < 20; i++) {
            Packet packet = new Packet("Data Packet " + i);
            boolean added = leakyBucket.addPacket(packet);
            if (added) {
                System.out.println("Packet added: " + packet.getData());
            } else {
                System.out.println("Packet dropped: " + packet.getData());
            }
            try {
                Thread.sleep(500); // 控制数据包产生的速率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
