package com.allround.snowflake;

import org.junit.Test;

import java.util.Date;

public class SnowflakeTest {
    private SnowflakeIDGenerator sf = new SnowflakeIDGenerator(1);

    @Test
    public void test(){
        for(int i=1;i<=10;i++){
            long id = sf.nextId();
            System.out.println(id);
        }
    }
    @Test
    public void test1(){
        SnowFlakeUtil snowFlakeUtil = new SnowFlakeUtil();
        for(int i=1;i<=10;i++){
            long id = snowFlakeUtil.nextId();
            System.out.println("全局唯一ID="+id);
        }

        /*System.out.println(id);
        Date date = SnowFlakeUtil.getTimeBySnowFlakeId(id);
        System.out.println(date);
        long time = date.getTime();
        System.out.println(time);
        System.out.println(SnowFlakeUtil.getRandomStr());*/
    }
}
