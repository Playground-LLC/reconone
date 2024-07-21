package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.ExceptionMessageLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedMessageLogger;
import com.company.reconone.common.processors.StartMessageLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseKafkaConsumerPipelineTest {

    private BaseKafkaConsumerPipeline pipeline;
    private MdcProcessor mdcProcessor;
    private StartMessageLogger startMessageLogger;
    private ProcessedMessageLogger processedMessageLogger;
    private ExceptionMessageLogger exceptionMessageLogger;

    @BeforeEach
    public void setUp() {
        mdcProcessor = Mockito.mock(MdcProcessor.class);
        startMessageLogger = Mockito.mock(StartMessageLogger.class);
        processedMessageLogger = Mockito.mock(ProcessedMessageLogger.class);
        exceptionMessageLogger = Mockito.mock(ExceptionMessageLogger.class);

        pipeline = new TestKafkaConsumerPipeline(mdcProcessor, startMessageLogger, processedMessageLogger, exceptionMessageLogger);
    }

    /**
     * Test the construction of the Kafka consumer URI.
     */
    @Test
    public void testConstructKafkaConsumer() {
        String expectedUri = "kafka:testTopic";
        assertEquals(expectedUri, pipeline.constructKafkaConsumer());
    }

    private static class TestKafkaConsumerPipeline extends BaseKafkaConsumerPipeline {

        @Value("${instance.id}")
        private String instanceId;

        public TestKafkaConsumerPipeline(MdcProcessor mdcProcessor, StartMessageLogger startMessageLogger,
                                         ProcessedMessageLogger processedMessageLogger, ExceptionMessageLogger exceptionMessageLogger) {
            super(mdcProcessor, startMessageLogger, processedMessageLogger, exceptionMessageLogger);
        }

        @Override
        public String getPipelineName() {
            return "testPipeline";
        }

        @Override
        public Object getProcessor() {
            return new Object() {
                public void process() {
                    // Mock processing logic
                }
            };
        }

        @Override
        public String kafkaTopic() {
            return "testTopic";
        }
    }
}
