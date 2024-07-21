package com.company.reconone.pipelines.pipeline3;

import com.company.reconone.common.processors.BaseFileProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.company.reconone.pipelines.pipeline3.Pipeline3Constants.Stages.*;

@Component
public class Pipeline3Processor extends BaseFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline3Processor.class);

    @Override
    public void processFile(Exchange exchange) {
        saveToDatabase(exchange);
        saveToMongo(exchange);
        sendToKafka(exchange);
    }

    public void saveToDatabase(Exchange exchange) {
        incrementProcessed(STAGE_1_MARIADB.getStage());
        logger.info("Saving to MariaDB");
    }

    public void saveToMongo(Exchange exchange) {
        incrementProcessed(STAGE_2_MONGODB.getStage());
        logger.info("Saving to MongoDB");
    }

    public void sendToKafka(Exchange exchange) {
        try {
            incrementProcessed(STAGE_3_KAFKA.getStage());
            String message = exchange.getIn().getBody(String.class);
            logger.info("Sent message to Kafka: {}", message);
        } catch (Exception e) {
            logger.error("Error sending to Kafka in stage {}", STAGE_3_KAFKA.getStage(), e);
        }
    }
}
