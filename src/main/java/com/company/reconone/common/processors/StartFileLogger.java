package com.company.reconone.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Processor to initialize the file logger.
 *
 * This processor initializes the file logger by setting the file name, file size, and start time.
 *
 **/
@Component
public class StartFileLogger implements Processor {

    @Override
    public void process(Exchange exchange) {
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
        long fileSize = exchange.getIn().getHeader("CamelFileLength", Long.class);
        long startTime = System.currentTimeMillis();
        exchange.setProperty("fileName", fileName);
        exchange.setProperty("fileSize", fileSize);
        exchange.setProperty("startTime", startTime);

        RecordCounter recordCounter = new RecordCounter();
        recordCounter.reset();
        exchange.setProperty("recordCounter", recordCounter);
    }
}

