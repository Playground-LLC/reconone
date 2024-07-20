package com.company.reconone.pipelines.pipeline1;

import com.company.reconone.common.processors.BaseFileProcessor;
import com.company.reconone.integration.ETLKafkaService;
import com.company.reconone.mongo.TransformedData;
import com.company.reconone.service.ProcessedDataService;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Pipeline1Processor extends BaseFileProcessor {

    @Autowired
    private ProcessedDataService processedDataService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ETLKafkaService etlKafkaService;

    private static final Logger logger = LoggerFactory.getLogger(Pipeline1Processor.class);

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
