package com.company.reconone.common.processors;

import com.company.reconone.common.domain.MessageProcessingInfo;
import com.company.reconone.common.repository.MessageProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMessageLogger.class);

    @Autowired
    private MessageProcessingRepository messageProcessingRepository;

    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        Long messageProcessingId = exchange.getProperty("messageProcessingId", Long.class);

        String stackTrace = getStackTrace(exception);
        logger.error("Error processing message: {}", stackTrace);

        MessageProcessingInfo messageProcessingInfo = messageProcessingRepository.findById(messageProcessingId)
                .orElse(new MessageProcessingInfo());

        messageProcessingInfo.setErrorStackTrace(stackTrace);
        messageProcessingInfo.setStatus("FAILED");

        messageProcessingRepository.save(messageProcessingInfo);
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
