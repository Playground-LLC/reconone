package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.ExceptionMessageLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedMessageLogger;
import com.company.reconone.common.processors.StartMessageLogger;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Base class for all Kafka consumer pipelines.
 * <p>
 * This class configures a Camel route to consume messages from a Kafka topic, process them, and log the processing details.
 * Subclasses must implement kafkaTopic(), getProcessor(), and getPipelineName() to specify the Kafka topic, processor, and pipeline name.
 */
@Component
@RequiredArgsConstructor
public abstract class BaseKafkaConsumerPipeline extends RouteBuilder {

    protected final MdcProcessor mdcProcessor;
    protected final StartMessageLogger startMessageLogger;
    protected final ProcessedMessageLogger processedMessageLogger;
    protected final ExceptionMessageLogger exceptionMessageLogger;
    @Value("${instance.id}")
    protected String instanceId;

    /**
     * Get the name of the pipeline.
     *
     * @return pipeline name
     */
    abstract public String getPipelineName();

    /**
     * Get the processor to handle message processing.
     *
     * @return processor instance
     */
    abstract public Object getProcessor();

    /**
     * Get the Kafka topic to consume messages from.
     *
     * @return Kafka topic name
     */
    abstract public String kafkaTopic();

    /**
     * Configure the Camel route to consume, process, and log messages from a Kafka topic.
     */
    @Override
    public void configure() {
        configureExceptionHandling();

        from(constructKafkaConsumer())
                .routeId(getPipelineName())
                .process(startMessageLogger)
                .process(mdcProcessor)
                .log("Processing message from topic: ${header.kafka.TOPIC}")
                .bean(getProcessor(), "process")
                .process(processedMessageLogger);
    }

    /**
     * Configure exception handling for the route.
     */
    private void configureExceptionHandling() {
        onException(Exception.class)
                .handled(true)
                .log("Error processing message from topic: ${header.kafka.TOPIC}")
                .process(exceptionMessageLogger);
    }

    /**
     * Construct the URI for the route's "from" endpoint to consume messages from the Kafka topic.
     *
     * @return constructed URI
     */
    protected String constructKafkaConsumer() {
        return "kafka:" + kafkaTopic();
    }
}
