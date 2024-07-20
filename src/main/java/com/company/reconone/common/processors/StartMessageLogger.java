package com.company.reconone.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Processor to initialize the message logger.
 *
 * This processor initializes the message logger by setting the message ID, start time, and other relevant properties.
 */
@Component
public class StartMessageLogger implements Processor {

    @Override
    public void process(Exchange exchange) {
        String messageId = exchange.getIn().getHeader("CamelMessageId", String.class);
        long startTime = System.currentTimeMillis();
        exchange.setProperty("messageId", messageId);
        exchange.setProperty("startTime", startTime);
    }
}
