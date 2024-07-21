package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static com.company.reconone.common.processors.CommonConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProcessedFileLoggerTest extends CamelTestSupport {

    private ProcessedFileLogger processedFileLogger;
    private FileProcessingRepository fileProcessingRepository;

    @BeforeEach
    public void setUp() {
        fileProcessingRepository = Mockito.mock(FileProcessingRepository.class);
        processedFileLogger = new ProcessedFileLogger(fileProcessingRepository);
    }

    /**
     * Test the proper logging of file processing completion details.
     */
    @Test
    public void testLogFileProcessingCompletion() {
        Exchange exchange = createExchangeWithProperties("testFile.txt", 12345L);
        exchange.setProperty(START_TIME_PROPERTY, System.currentTimeMillis() - 1000);

        RecordCounter recordCounter = new RecordCounter();
        recordCounter.incrementProcessed("test stage");
        exchange.setProperty(RECORD_COUNTER_PROPERTY, recordCounter);

        FileProcessingInfo fileProcessingInfo = new FileProcessingInfo();
        fileProcessingInfo.setId(1L);
        when(fileProcessingRepository.save(any(FileProcessingInfo.class))).thenReturn(fileProcessingInfo);

        processedFileLogger.process(exchange);

        assertEquals(1L, exchange.getProperty(FILE_PROCESSING_ID_PROPERTY));

        verify(fileProcessingRepository).save(any(FileProcessingInfo.class));
    }

    /**
     * Test handling of exceptions during the database update operation.
     */
    @Test
    public void testHandleDatabaseUpdateException() {
        Exchange exchange = createExchangeWithProperties("testFile.txt", 12345L);

        doThrow(new RuntimeException("Database error")).when(fileProcessingRepository).save(any(FileProcessingInfo.class));

        Logger logger = Mockito.mock(Logger.class);
        ProcessedFileLogger.logger = logger;

        processedFileLogger.process(exchange);

        verify(logger).error(eq("Error updating FileProcessingInfo in the database for file: {}"), eq("testFile.txt"), any(RuntimeException.class));
    }

    private Exchange createExchangeWithProperties(String fileName, Long fileSize) {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.setProperty(FILE_NAME_PROPERTY, fileName);
        exchange.setProperty(FILE_SIZE_PROPERTY, fileSize);
        exchange.setProperty(FILE_PROCESSING_ID_PROPERTY, 1L);
        return exchange;
    }
}
