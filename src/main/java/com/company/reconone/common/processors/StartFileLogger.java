package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.company.reconone.common.processors.CommonConstants.*;

/**
 * Processor to initialize the file logger.
 * <p>
 * This processor initializes the file logger by setting the file name, file size, and start time.
 */
@Component
public class StartFileLogger implements Processor {
    static Logger logger = LoggerFactory.getLogger(StartFileLogger.class);

    private final FileProcessingRepository fileProcessingRepository;
    private final String instanceId;

    public StartFileLogger(FileProcessingRepository fileProcessingRepository, @Value("${instance.id}") String instanceId) {
        this.fileProcessingRepository = fileProcessingRepository;
        this.instanceId = instanceId;
    }

    @Override
    public void process(Exchange exchange) {
        initializeFileLoggingProperties(exchange);
        resetRecordCounter(exchange);
    }

    /**
     * Initializes file logging properties such as file name, size, and start time.
     *
     * @param exchange the Camel exchange containing the file data
     */
    private void initializeFileLoggingProperties(Exchange exchange) {
        String fileName = Optional.ofNullable(exchange.getIn().getHeader(CAMEL_FILE_NAME_HEADER, String.class)).orElse("Unknown");
        Long fileSize = Optional.ofNullable(exchange.getIn().getHeader(CAMEL_FILE_LENGTH_HEADER, Long.class)).orElse(0L);
        long startTime = System.currentTimeMillis();

        setExchangeProperty(exchange, FILE_NAME_PROPERTY, fileName);
        setExchangeProperty(exchange, FILE_SIZE_PROPERTY, fileSize);
        setExchangeProperty(exchange, START_TIME_PROPERTY, startTime);

        // Save to database
        try {
            FileProcessingInfo fileProcessingInfo = new FileProcessingInfo();
            fileProcessingInfo.setFileName(fileName);
            fileProcessingInfo.setFileSize(fileSize);
            fileProcessingInfo.setStartTime(startTime);
            fileProcessingInfo.setStatus("STARTED");
            fileProcessingInfo.setPipelineId(exchange.getFromRouteId());
            fileProcessingInfo.setInstanceId(instanceId);

            fileProcessingInfo = fileProcessingRepository.save(fileProcessingInfo);

            setExchangeProperty(exchange, FILE_PROCESSING_ID_PROPERTY, fileProcessingInfo.getId());
        } catch (Exception e) {
            logger.error("Error saving FileProcessingInfo to the database for file: {}", fileName, e);
        }
    }

    /**
     * Resets the record counter in the exchange properties.
     *
     * @param exchange the Camel exchange
     */
    private void resetRecordCounter(Exchange exchange) {
        RecordCounter recordCounter = new RecordCounter();
        setExchangeProperty(exchange, RECORD_COUNTER_PROPERTY, recordCounter);
    }

    /**
     * Sets a property in the exchange.
     *
     * @param exchange the Camel exchange
     * @param key      the property key
     * @param value    the property value
     */
    private void setExchangeProperty(Exchange exchange, String key, Object value) {
        exchange.setProperty(key, value);
    }
}
