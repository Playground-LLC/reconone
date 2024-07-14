package com.company.reconone.pipelines.pipeline2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class Pipeline2Processor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Transformation logic here
        String originalBody = exchange.getIn().getBody(String.class);
        String transformedBody = originalBody.replace("oldValue", "newValue"); // Example transformation
        exchange.getIn().setBody(transformedBody);
    }
}