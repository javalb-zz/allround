package com.allround.kafka;
import com.allround.mongodb.MongoDBJDBC;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;

public class KafkaTest {

    private ProducerDemo producerDemo = new ProducerDemo();
    private ConsumerDemo consumerDemo = new ConsumerDemo();

    @Test
    public void sendMessage() {
        producerDemo.sendMessage();
    }

    @Test
    public void receiveMessage() {
        consumerDemo.receMessage();
    }



}
