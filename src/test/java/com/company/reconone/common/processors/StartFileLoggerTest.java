package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static com.company.reconone.common.processors.CommonConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StartFileLoggerTest extends CamelTestSupport {

    private StartFileLogger startFileLogger;
    private FileProcessingRepository fileProcessingRepository;
    private String instanceId = "testInstance";

    @BeforeEach
    public void setUp() {
        fileProcessingRepository = Mockito.mock(FileProcessingRepository.class);
        startFileLogger = new StartFileLogger(fileProcessingRepository, instanceId);
    }

    /**
     * Test the proper initialization of file logging properties.
     */
    @Test
    public void testInitializeFileLoggingProperties() {

        FileProcessingInfo fileProcessingInfo = new FileProcessingInfo();
        fileProcessingInfo.setId(1L);
        fileProcessingInfo.setInstanceId(instanceId);
        when(fileProcessingRepository.save(any(FileProcessingInfo.class))).thenReturn(fileProcessingInfo);

        Exchange exchange = createExchangeWithProperties("testFile.txt", 12345L);

        startFileLogger.process(exchange);

        assertEquals("testFile.txt", exchange.getProperty(FILE_NAME_PROPERTY));
        assertEquals(12345L, exchange.getProperty(FILE_SIZE_PROPERTY));
        assertEquals(1L, exchange.getProperty(FILE_PROCESSING_ID_PROPERTY));
    }

    /**
     * Test handling of exceptions during the database save operation.
     */
    @Test
    public void testHandleDatabaseSaveException() {
        Exchange exchange = createExchangeWithProperties("testFile.txt", 12345L);

        doThrow(new RuntimeException("Database error")).when(fileProcessingRepository).save(any(FileProcessingInfo.class));

        Logger logger = Mockito.mock(Logger.class);
        StartFileLogger.logger = logger;

        startFileLogger.process(exchange);

        verify(logger).error(eq("Error saving FileProcessingInfo to the database for file: {}"), eq("testFile.txt"), any(RuntimeException.class));
    }

    private Exchange createExchangeWithProperties(String fileName, Long fileSize) {
        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        Message message = new DefaultMessage(exchange);
        message.setHeader(CAMEL_FILE_NAME_HEADER, fileName);
        message.setHeader(CAMEL_FILE_LENGTH_HEADER, fileSize);
        exchange.setIn(message);
        return exchange;
    }
}
