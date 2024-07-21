package com.company.reconone.pipelines.pipeline1;

import com.company.reconone.common.processors.BaseFileProcessor;
import com.company.reconone.integration.ETLKafkaService;
import com.company.reconone.mongo.TransformedData;
import com.company.reconone.service.ProcessedDataService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Pipeline1Processor extends BaseFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline1Processor.class);

    private final ProcessedDataService processedDataService;
    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ETLKafkaService etlKafkaService;

    @Override
    public void processFile(Exchange exchange) {
        // Process the exchange
        saveToDatabase(exchange);
        saveToMongo(exchange);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendToKafka(exchange);
    }

    public void saveToDatabase(Exchange exchange) {
        incrementProcessed("saveToDatabase");
        processedDataService.saveProcessedData(exchange.getIn().getBody(String.class));
        logger.info("Saved to MariaDB");
    }

    public void saveToMongo(Exchange exchange) {
        incrementProcessed("saveToMongo");
        String data = exchange.getIn().getBody(String.class);
        mongoTemplate.save(new TransformedData(data));
        logger.info("Saved to MongoDB");
    }

    public void sendToKafka(Exchange exchange) {
        incrementSkipped("sendToKafka");
        etlKafkaService.sendToKafka(exchange.getIn().getBody(String.class));
    }
}
