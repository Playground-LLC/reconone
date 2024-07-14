package com.company.reconone.pipelines.pipeline1;

import com.company.reconone.common.processors.BaseFileProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Pipeline1Processor extends BaseFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline1Processor.class);

    @Override
    public void processFile(Exchange exchange) {
        // Process the exchange
        saveToDatabase(exchange);
        saveToMongo(exchange);
        sendToKafka(exchange);
    }

    public void saveToDatabase(Exchange exchange) {
        incrementProcessed("saveToDatabase");

        logger.info("Saving to MariaDB");
    }

    public void saveToMongo(Exchange exchange) {
        incrementProcessed("saveToMongo");

        logger.info("Saving to Mongodb");
    }

    public void sendToKafka(Exchange exchange) {
        incrementSkipped("saveToMongo");

        String message = exchange.getIn().getBody(String.class);
        logger.info("Sent message to Kafka: {}", message);
    }
}
