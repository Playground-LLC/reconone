package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExceptionFileLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionFileLogger.class);

    @Autowired
    private FileProcessingRepository fileProcessingRepository;

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        Long fileProcessingId = exchange.getProperty("fileProcessingId", Long.class);

        String stackTrace = getStackTrace(exception);
        logger.error("Error processing file: {}", stackTrace);

        FileProcessingInfo fileProcessingInfo = fileProcessingRepository.findById(fileProcessingId)
                        .orElse(new FileProcessingInfo());

        fileProcessingInfo.setErrorStackTrace(stackTrace);
        fileProcessingInfo.setStatus("FAILED");

        fileProcessingRepository.save(fileProcessingInfo);
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}

