package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.company.reconone.common.processors.CommonConstants.*;

/**
 * Processor that logs information about a processed file.
 * <p>
 * This processor logs information about a processed file, including the file name, size, start time, end time, time taken, and number of records processed and skipped.
 */
@Component
public class ProcessedFileLogger implements Processor {
    static Logger logger = LoggerFactory.getLogger(ProcessedFileLogger.class);

    private final FileProcessingRepository fileProcessingRepository;

    public ProcessedFileLogger(FileProcessingRepository fileProcessingRepository) {
        this.fileProcessingRepository = fileProcessingRepository;
    }

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
        Long startTime = exchange.getProperty(START_TIME_PROPERTY, Long.class);
        startTime = Optional.ofNullable(startTime).orElse(0L);
        long timeTaken = endTime - startTime;

        String fileName = exchange.getProperty(FILE_NAME_PROPERTY, String.class);
        fileName = Optional.ofNullable(fileName).orElse("Unknown");

        Long fileSize = exchange.getProperty(FILE_SIZE_PROPERTY, Long.class);
        fileSize = Optional.ofNullable(fileSize).orElse(0L);

        Long fileProcessingId = exchange.getProperty(FILE_PROCESSING_ID_PROPERTY, Long.class);
        fileProcessingId = Optional.ofNullable(fileProcessingId).orElse(0L);

        RecordCounter recordCounter = exchange.getProperty(RECORD_COUNTER_PROPERTY, RecordCounter.class);
        recordCounter = Optional.ofNullable(recordCounter).orElse(new RecordCounter());

        logger.info("File processing completed. File: {}, Size: {}, Start time: {}, End time: {}, Time taken: {}, Records processed: {}, Records skipped: {}",
                fileName, fileSize, startTime, endTime, timeTaken,
                recordCounter.getAllRecordsProcessed(), recordCounter.getAllRecordsSkipped());

        try {
            // Update database
            FileProcessingInfo fileProcessingInfo = fileProcessingRepository.findById(fileProcessingId)
                    .orElse(new FileProcessingInfo());
            fileProcessingInfo.setEndTime(endTime);
            fileProcessingInfo.setTimeTaken(timeTaken);
            fileProcessingInfo.setStatus("COMPLETED");
            fileProcessingInfo.setRecordsProcessed(recordCounter.getAllRecordsProcessed());
            fileProcessingInfo.setRecordsSkipped(recordCounter.getAllRecordsSkipped());

            fileProcessingRepository.save(fileProcessingInfo);
        } catch (Exception e) {
            logger.error("Error updating FileProcessingInfo in the database for file: {}", fileName, e);
        }
    }
}
