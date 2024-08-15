package com.allround.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ProducerDemo {
    public void  sendMessage () {
        // 设置配置属性
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建 Kafka 生产者实例
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // 发送消息
            for (int i = 0; i < 3; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>("my-topic", "key-" + i, "sendvalue-" + i);
                producer.send(record);
            }
            // 确保所有消息都被发送出去
            producer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}