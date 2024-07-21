package com.company.reconone.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "processedData")
@Data
public class TransformedData {
    @Id
    private String id;
    private String data;

    public TransformedData(String data) {
        this.data = data;
    }
}
