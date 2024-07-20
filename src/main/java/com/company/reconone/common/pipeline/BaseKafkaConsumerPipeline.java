package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedMessageLogger;
import com.company.reconone.common.processors.StartMessageLogger;
import com.company.reconone.common.processors.ExceptionMessageLogger;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.spi.RouteController;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Base class for all Kafka consumer pipelines.
 * <p>
 * This class configures a Camel route to consume messages from a Kafka topic, process them, and log the processing details.
 * Subclasses must implement kafkaTopic(), getProcessor(), and getPipelineName() to specify the Kafka topic, processor, and pipeline name.
 */
@Component
public abstract class BaseKafkaConsumerPipeline extends RouteBuilder {

    @Value("${instance.id}")
    protected String instanceId;

    @Autowired
    protected MdcProcessor mdcProcessor;

    @Autowired
    protected StartMessageLogger startMessageLogger;

    @Autowired
    protected ProcessedMessageLogger processedMessageLogger;

    @Autowired
    protected ExceptionMessageLogger exceptionMessageLogger;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CamelContext camelContext;

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

        from(constructFromRoute())
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
    private String constructFromRoute() {
        return "kafka:" + kafkaTopic();
    }
}
