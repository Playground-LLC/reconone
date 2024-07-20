package com.company.reconone.pipelines.pipeline2;

import com.company.reconone.common.pipeline.BaseKafkaConsumerPipeline;
import com.company.reconone.pipelines.PipelineNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(PipelineNames.PIPELINE_2)
public class Pipeline2RouteBuilder extends BaseKafkaConsumerPipeline {

    @Value("${pipeline2.source.kafka.topic}")
    private String kafkaTopic;

    @Autowired
    private Pipeline2Processor pipeline2Processor;

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