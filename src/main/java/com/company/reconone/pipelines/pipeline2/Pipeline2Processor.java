package com.company.reconone.pipelines.pipeline2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class Pipeline2Processor {

    public void process(Exchange exchange) throws Exception {
        // Transformation logic here
        String originalBody = exchange.getIn().getBody(String.class);
        String transformedBody = originalBody.replace("oldValue", "newValue"); // Example transformation
        exchange.getIn().setBody(transformedBody);
    }
}