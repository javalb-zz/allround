package com.allround.elsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;

import java.util.HashMap;
import java.util.Map;

public class ElasticsearchExample {

    private static RestHighLevelClient client;

    public static void main(String[] args) throws Exception{
        // 创建 RestClient 实例
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        RequestOptions options = RequestOptions.DEFAULT;
        // 测试连接
        try {
            boolean connected = client.ping(options);
            if (connected) {
                System.out.println("Connected to Elasticsearch cluster!");
            } else {
                System.out.println("Failed to connect to Elasticsearch cluster.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 执行增删改查操作
        createDocument();
        getDocument();
        //updateDocument();
        //deleteDocument();

        // 关闭客户端
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建文档
    private static void createDocument() {
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("name", "John Doe2");
            jsonMap.put("age", 31);

            IndexRequest indexRequest = new IndexRequest("myindex2")
                    .id("2")
                    .source(jsonMap);

            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("Index Response: " + indexResponse.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取文档
    private static void getDocument() {
        try {
            GetRequest getRequest = new GetRequest("myindex2", "2");
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

            if (getResponse.isExists()) {
                System.out.println("Name: " + getResponse.getSource().get("name"));
                System.out.println("Age: " + getResponse.getSource().get("age"));
            } else {
                System.out.println("Document not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新文档
    private static void updateDocument() {
        try {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("age", 31);

            UpdateRequest updateRequest = new UpdateRequest("myindex", "1")
                    .doc(jsonMap);

            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println("Update Response: " + updateResponse.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除文档
    private static void deleteDocument() {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("myindex", "1");
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println("Delete Response: " + deleteResponse.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}