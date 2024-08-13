package com.allround.mongodb;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
public class MognodbTest {

    private MongoDBJDBC jdbc = new MongoDBJDBC();

    MongoClient mongoClient = MongoDBJDBC.getMongoClient();

    MongoCollection<Document> collection = MongoDBJDBC.getMongoCollection();

    @Test
    public void testAdd() {
        jdbc.mongodbInsert(collection);
        mongoClient.close();
    }

    @Test
    public void testQuery() {
        jdbc.mongodbQuey(collection);
        mongoClient.close();
    }

    @Test
    public void testUpdate() {
        jdbc.mongodbUpdate(collection);
        mongoClient.close();
    }

    @Test
    public void testDelete() {
        jdbc.deleteDocument(collection);
        mongoClient.close();
    }

}
