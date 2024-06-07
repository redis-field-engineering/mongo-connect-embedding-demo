package com.redis;

import org.bson.Document;

public class Message {
    private int id;
    private String username;
    private String message;
    private long timestamp;

    public Message(int id, String username, String message, long timestamp){
        this.id = id;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    Document toDocument() {
        return new Document("id", id)
                .append("username", username)
                .append("message", message)
                .append("timestamp", timestamp);
    }
}
