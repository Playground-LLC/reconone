package com.company.reconone.pipelines.pipeline2;

import com.company.reconone.common.pipeline.BaseKafkaConsumerPipeline;
import com.company.reconone.common.processors.ExceptionMessageLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedMessageLogger;
import com.company.reconone.common.processors.StartMessageLogger;
import com.company.reconone.pipelines.PipelineNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(PipelineNames.PIPELINE_2)
public class Pipeline2RouteBuilder extends BaseKafkaConsumerPipeline {

    private final Pipeline2Processor pipeline2Processor;
    @Value("${pipeline2.source.kafka.topic}")
    private String kafkaTopic;

    public Pipeline2RouteBuilder(MdcProcessor mdcProcessor,
                                 StartMessageLogger startMessageLogger,
                                 ProcessedMessageLogger processedMessageLogger,
                                 ExceptionMessageLogger exceptionMessageLogger,
                                 Pipeline2Processor pipeline2Processor) {
        super(mdcProcessor, startMessageLogger, processedMessageLogger, exceptionMessageLogger);
        this.pipeline2Processor = pipeline2Processor;
    }

    @Override
    public String getPipelineName() {
        return PipelineNames.PIPELINE_2;
    }

    @Override
    public Object getProcessor() {
        return pipeline2Processor;
    }

    @Override
    public String kafkaTopic() {
        return kafkaTopic;
    }
}