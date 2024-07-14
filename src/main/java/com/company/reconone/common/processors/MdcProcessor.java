package com.company.reconone.common.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Processor that adds the pipelineId to the MDC context. The pipelineId is the ID of the pipeline that is processing the file.
 *
 * The pipelineId is added to the MDC context so that it can be included in log messages.
 */
@Component
public class MdcProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String pipelineId = exchange.getFromRouteId();
        MDC.put("pipelineId", pipelineId);
    }
}

