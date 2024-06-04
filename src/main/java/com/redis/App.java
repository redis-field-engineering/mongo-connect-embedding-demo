package com.redis;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String connectionString = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoDatabase database = mongoClient.getDatabase("testdb");
        MongoCollection<Document> collection = database.getCollection("customers");

        List<Document> customerDocuments = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            Customer customer = new Customer(
                    UUID.randomUUID().toString(),
                    getRandomFirstName(),
                    getRandomLastName(),
                    getRandomEmail(),
                    getRandomPhoneNumber()
            );

            customerDocuments.add(customer.toDocument());

            // Insert in batches of 1000
            if (customerDocuments.size() == 1000) {
                collection.insertMany(customerDocuments);
                customerDocuments.clear();
                System.out.printf("Saved %d customer documents%n", i + 1);
            }
        }

        // Insert remaining documents if any
        if (!customerDocuments.isEmpty()) {
            collection.insertMany(customerDocuments);
        }

        mongoClient.close();
    }

    private static String getRandomFirstName() {
        String[] firstNames = {"John", "Jane", "Alex", "Emily", "Michael", "Sarah", "David", "Laura"};
        return firstNames[ThreadLocalRandom.current().nextInt(firstNames.length)];
    }

    private static String getRandomLastName() {
        String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson"};
        return lastNames[ThreadLocalRandom.current().nextInt(lastNames.length)];
    }

    private static String getRandomEmail() {
        String[] domains = {"example.com", "test.com", "mail.com", "domain.com"};
        return getRandomFirstName().toLowerCase() + "." + getRandomLastName().toLowerCase() + "@" + domains[ThreadLocalRandom.current().nextInt(domains.length)];
    }

    private static String getRandomPhoneNumber() {
        return String.format("%03d-%03d-%04d", ThreadLocalRandom.current().nextInt(1000), ThreadLocalRandom.current().nextInt(1000), ThreadLocalRandom.current().nextInt(10000));
    }
}
