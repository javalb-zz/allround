package com.allround.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

public class MongoDBJDBC{

    // 创建MongoClient实例
    private static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    // 获取MongoDatabase实例
    private static MongoDatabase database = mongoClient.getDatabase("javatest");
    // 获取MongoCollection实例
    private static MongoCollection<Document> collection = database.getCollection("documents");

    public void mongodbQuey(MongoCollection collection){
        // 查询文档
        Document query = new Document("name", "jay");
        Document foundDocument = (Document) collection.find(query).first();
        if (foundDocument != null) {
            System.out.println("Found document: " + foundDocument.toJson());
        } else {
            System.out.println("No document found.");
        }
    }


    public void mongodbInsert(MongoCollection collection){
        // 插入文档
        Document document = new Document("name", "jay3")
                .append("age", 50)
                .append("city", "New York");
        InsertOneResult insertOneResult = collection.insertOne(document);
        System.out.println(insertOneResult.getInsertedId());
    }

    public void mongodbUpdate(MongoCollection<Document> collection) {
        Document query = new Document("name", "John Doe");
        Document update = new Document("$set", new Document("age", 35));
        UpdateResult updateResult = collection.updateOne(query, update);
        System.out.println(updateResult);
    }

    public void deleteDocument(MongoCollection<Document> collection) {
        Document query = new Document("name", "jay");
        DeleteResult deleteResult = collection.deleteOne(query);
        System.out.println("Document deleted."+deleteResult.getDeletedCount());
    }

    public static MongoClient getMongoClient(){
        return mongoClient;
    }

    public static MongoDatabase getDatabase(){
        return database;
    }

    public static MongoCollection getMongoCollection(){
        return collection;
    }


}
