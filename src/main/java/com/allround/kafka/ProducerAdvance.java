package com.allround.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class ProducerAdvance {
    public void  sendMessage () {
        // 设置配置属性
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        props.put("acks", "all"); // 等待所有副本的确认
        props.put("retries", 0); // 失败重试次数
        props.put("batch.size", 16384); // 批处理大小
        props.put("linger.ms", 1); // 批处理等待时间
        props.put("buffer.memory", 33554432); // 缓冲区大小
        props.put("compression.type", "gzip"); // 消息压缩类型

        // 创建 Kafka 生产者实例
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            // 发送消息
            for (int i = 0; i < 2; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>("my-topic1",  "key-" + i, "value-" + i);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Message sent successfully: " + metadata.toString());
                    } else {
                        System.out.println("Error sending message: " + exception.getMessage());
                    }
                });
            }
            // 确保所有消息都被发送出去
            producer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}