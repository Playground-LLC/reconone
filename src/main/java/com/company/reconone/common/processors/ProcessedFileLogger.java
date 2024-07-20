package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Processor that logs information about a processed file.
 * <p>
 * This processor logs information about a processed file, including the file name, size, start time, end time, time taken, and number of records processed and skipped.
 */
@Component
public class ProcessedFileLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessedFileLogger.class);

    @Autowired
    private FileProcessingRepository fileProcessingRepository;

    @Override
    public void process(Exchange exchange) {
        logFileProcessingCompletion(exchange);
    }

    /**
     * Logs the completion of file processing including file details and processing statistics.
     *
     * @param exchange the Camel exchange containing the file data and processing details
     */
    private void logFileProcessingCompletion(Exchange exchange) {
        long endTime = System.currentTimeMillis();
        long startTime = exchange.getProperty("startTime", Long.class);
        long timeTaken = endTime - startTime;
        String fileName = exchange.getProperty("fileName", String.class);
        long fileSize = exchange.getProperty("fileSize", Long.class);
        Long fileProcessingId = exchange.getProperty("fileProcessingId", Long.class);

        RecordCounter recordCounter = exchange.getProperty("recordCounter", RecordCounter.class);

        logger.info("File processing completed. File: {}, Size: {}, Start time: {}, End time: {}, Time taken: {}, Records processed: {}, Records skipped: {}",
                fileName, fileSize, startTime, endTime, timeTaken,
                recordCounter.getAllRecordsProcessed(), recordCounter.getAllRecordsSkipped());

        // Update database
        FileProcessingInfo fileProcessingInfo = fileProcessingRepository.findById(fileProcessingId)
                .orElse(new FileProcessingInfo());
        fileProcessingInfo.setEndTime(endTime);
        fileProcessingInfo.setTimeTaken(timeTaken);
        fileProcessingInfo.setStatus("COMPLETED");
        fileProcessingInfo.setRecordsProcessed(recordCounter.getAllRecordsProcessed());
        fileProcessingInfo.setRecordsSkipped(recordCounter.getAllRecordsSkipped());

        fileProcessingRepository.save(fileProcessingInfo);
    }
}
