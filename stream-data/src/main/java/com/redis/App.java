package com.redis;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        String connectionString = "mongodb://redisconnect:Redis123@mongo:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoDatabase database = mongoClient.getDatabase("testdb");

        MongoCollection<Document> messagesCollection = database.getCollection("messages");



        int i = 0;
        while(true){
            Message message = new Message(
                    i+1,
                    getRandomUsername(),
                    getRandomMessage(),
                    System.currentTimeMillis()
            );
            messagesCollection.insertOne(message.toDocument());
            i++;
            Thread.sleep(1000);
        }
    }

    private static String getRandomUsername() {
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"};
        return usernames[ThreadLocalRandom.current().nextInt(usernames.length)];
    }

    private static String getRandomMessage() {
        String[] messages = {"Hello", "Good morning", "Good afternoon", "Good evening", "Good night"};
        return messages[ThreadLocalRandom.current().nextInt(messages.length)];
    }
}
