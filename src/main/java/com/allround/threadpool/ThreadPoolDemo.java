package com.allround.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {

    /**
     * CachedThreadPool
     * 使用场景：适用于执行大量短生命周期任务的场景，因为它可以迅速地创建和销毁线程，以适应不断变化的负载。
     * 特点：核心线程数为0，最大线程数为Integer.MAX_VALUE，如果任务很多，会创建大量线程，直到耗尽系统资源。
     */
    public void cachedThreadPoolExample(){
        ExecutorService executor = Executors.newCachedThreadPool();;
        for (int i = 0; i < 5; i++) {
            final int taskNumber = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Task " + taskNumber + " executed by " + Thread.currentThread().getName());
                }
            });
        }
        executor.shutdown();
    }

    /**
     * FixedThreadPool
     * 使用场景：适用于执行固定数量的并发任务，且任务数量不会频繁变化的场景。
     * 特点：核心线程数等于最大线程数，线程池中的线程数量始终不变。
     */
    public void fixThreadPoolExample(){
        ExecutorService executor = Executors.newFixedThreadPool(4); // 固定大小为4的线程池
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Task " + taskNumber + " executed by " + Thread.currentThread().getName());
                }
            });
        }
        executor.shutdown();
    }

    /**
     * SingleThreadExecutor：
     * 使用场景：适用于需要单个线程顺序执行多个任务的场景，保证任务的顺序性。
     * 特点：核心线程数和最大线程数都为1。
     */
    public void singleThreadExecutorExample(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Task " + taskNumber + " executed by " + Thread.currentThread().getName());
                }
            });
        }
        executor.shutdown();
    }

    /**
     * ScheduledThreadPool：
     * 场景：一个智能家居系统，需要在每天固定时间执行一些任务，比如早上7点自动打开窗帘，晚上10点自动关灯。
     * 说明：使用ScheduledThreadPool可以设置定时任务，确保智能家居系统在指定时间执行相应的操作。
     */
    public void scheduledThreadPoolExample(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task executed at " + System.currentTimeMillis());
            }
        }, 0, 2, TimeUnit.SECONDS); // 每2秒执行一次
    }
}
