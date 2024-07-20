package com.company.reconone.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Processor that logs information about a processed message.
 *
 * This processor logs information about a processed message, including the message ID, start time, end time, time taken, and number of records processed and skipped.
 */
@Component
public class ProcessedMessageLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessedMessageLogger.class);

    @Override
    public void process(Exchange exchange) {
        long endTime = System.currentTimeMillis();
        long startTime = exchange.getProperty("startTime", Long.class);
        long timeTaken = endTime - startTime;
        String messageId = exchange.getProperty("messageId", String.class);

        logger.info("Message processing completed. Message ID: {}, Start time: {}, End time: {}, Time taken: {}",
                messageId, startTime, endTime, timeTaken);
    }
}
