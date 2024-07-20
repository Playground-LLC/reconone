package com.company.reconone.pipelines.pipeline3;

import com.company.reconone.common.pipeline.BaseFolderWatcherPipeline;
import com.company.reconone.common.processors.BaseFileProcessor;
import com.company.reconone.pipelines.PipelineNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(PipelineNames.PIPELINE_3)
public class Pipeline3RouteBuilder extends BaseFolderWatcherPipeline {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline3RouteBuilder.class);

    @Value("${pipeline3.source.folder}")
    private String sourceFolder;

    @Value("${pipeline3.target.folder}")
    private String targetFolder;

    @Autowired
    private Pipeline3Processor pipeline1Processor;

    @Override
    public String getPipelineName() {
        return PipelineNames.PIPELINE_3;
    }

    @Override
    public BaseFileProcessor getProcessor() {
        return pipeline1Processor;
    }

    @Override
    public String sourceFolder() {
        return sourceFolder;
    }

    @Override
    public String destinationFolder() {
        return targetFolder;
    }

    @Override
    public int chunkSize() {
        return 5;
    }

    @Override
    public String fileExtension() {
        return "txt";
    }
}

