package com.company.reconone.pipelines.pipeline2;

import com.company.reconone.common.processors.MdcProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pipeline2")
public class Pipeline2RouteBuilder extends RouteBuilder {

    @Value("${pipeline2.source.kafka.topic}")
    private String kafkaTopic;

    @Autowired
    private MdcProcessor mdcProcessor;

    @Override
    public void configure() {
        from("kafka:" + kafkaTopic + "?brokers=localhost:9092")
                .routeId("pipeline2")
                .process(mdcProcessor)
                .log("Processing message")
                .process(new Pipeline2Processor())
                .log("Completed message processing");
    }
}