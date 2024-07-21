package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.company.reconone.common.processors.CommonConstants.*;

@Component
public class ExceptionFileLogger implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionFileLogger.class);

    private final FileProcessingRepository fileProcessingRepository;

    public ExceptionFileLogger(FileProcessingRepository fileProcessingRepository) {
        this.fileProcessingRepository = fileProcessingRepository;
    }


    @Override
    public void process(Exchange exchange) {
        Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
        Long fileProcessingId = exchange.getProperty(CommonConstants.FILE_PROCESSING_ID_PROPERTY, Long.class);

        String stackTrace = getStackTrace(exception);
        logger.error("Error processing file: {}", stackTrace);

        try {
            FileProcessingInfo fileProcessingInfo = fileProcessingRepository.findById(fileProcessingId)
                    .orElse(new FileProcessingInfo());

            fileProcessingInfo.setErrorStackTrace(stackTrace);
            fileProcessingInfo.setStatus(STATUS_FAILED);

            fileProcessingRepository.save(fileProcessingInfo);
        } catch (Exception e) {
            logger.error("Error updating FileProcessingInfo in the database for fileProcessingId: {}", fileProcessingId, e);
        }
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}

