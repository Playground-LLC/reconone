package com.company.reconone.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "processedData")
public class TransformedData {
    @Id
    private String id;
    private String data;

    public TransformedData(String data) {
        this.data = data;
    }

    // Getters and setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
