package com.redis;

import org.bson.Document;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // Getters and setters

    public Customer(String id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // toDocument method to convert Customer to Document
    public Document toDocument() {
        return new Document("id", id)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("phoneNumber", phoneNumber);
    }
}
