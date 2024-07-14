package com.company.reconone.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Processor that logs information about a processed file.
 *
 * This processor logs information about a processed file, including the file name, size, start time, end time, time taken, and number of records processed and skipped.
 */
@Component
public class ProcessedFileLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessedFileLogger.class);

    @Override
    public void process(Exchange exchange) {
        long endTime = System.currentTimeMillis();
        long startTime = exchange.getProperty("startTime", Long.class);
        long timeTaken = endTime - startTime;
        String fileName = exchange.getProperty("fileName", String.class);
        long fileSize = exchange.getProperty("fileSize", Long.class);

        RecordCounter recordCounter = exchange.getProperty("recordCounter", RecordCounter.class);

        logger.info("File processing completed. File: {}, Size: {}, Start time: {}, End time: {}, Time taken: {}, Records processed: {}, Records skipped: {}",
                fileName, fileSize, startTime, endTime, timeTaken,
                recordCounter.getAllRecordsProcessed(), recordCounter.getAllRecordsSkipped());
    }
}

