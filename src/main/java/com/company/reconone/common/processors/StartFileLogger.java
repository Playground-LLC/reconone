package com.company.reconone.common.processors;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.domain.PipelineId;
import com.company.reconone.common.repository.FileProcessingRepository;
import com.company.reconone.common.repository.PipelineRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Processor to initialize the file logger.
 * <p>
 * This processor initializes the file logger by setting the file name, file size, and start time.
 */
@Component
public class StartFileLogger implements Processor {

    @Value("${instance.id}")
    protected String instanceId;
    @Autowired
    private PipelineRepository pipelineRepository;
    @Autowired
    private FileProcessingRepository fileProcessingRepository;

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
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
        long fileSize = exchange.getIn().getHeader("CamelFileLength", Long.class);
        long startTime = System.currentTimeMillis();

        exchange.setProperty("fileName", fileName);
        exchange.setProperty("fileSize", fileSize);
        exchange.setProperty("startTime", startTime);

        // Save to database
        FileProcessingInfo fileProcessingInfo = new FileProcessingInfo();
        fileProcessingInfo.setFileName(fileName);
        fileProcessingInfo.setFileSize(fileSize);
        fileProcessingInfo.setStartTime(startTime);
        fileProcessingInfo.setStatus("STARTED");
        fileProcessingInfo.setPipelineId(exchange.getFromRouteId());
        fileProcessingInfo.setInstanceId(instanceId);

        fileProcessingRepository.save(fileProcessingInfo);

        exchange.setProperty("fileProcessingId", fileProcessingInfo.getId());
    }

    /**
     * Resets the record counter in the exchange properties.
     *
     * @param exchange the Camel exchange
     */
    private void resetRecordCounter(Exchange exchange) {
        RecordCounter recordCounter = new RecordCounter();
        recordCounter.reset();
        exchange.setProperty("recordCounter", recordCounter);
    }
}
