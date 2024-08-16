package com.allround.threadpool;

import org.junit.Test;
public class ThreadTest {
    private ThreadPoolDemo tpd = new ThreadPoolDemo();

    @Test
    public void test1(){
        tpd.cachedThreadPoolExample();
    }
    @Test
    public void test2(){
        tpd.fixThreadPoolExample();
    }

    @Test
    public void test3(){
        tpd.singleThreadExecutorExample();
    }

    @Test
    public void test4(){
        tpd.scheduledThreadPoolExample();
    }
}
