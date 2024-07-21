package com.company.reconone.pipelines.pipeline3;

import com.company.reconone.common.pipeline.BaseFolderWatcherPipeline;
import com.company.reconone.common.processors.*;
import com.company.reconone.pipelines.PipelineNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(PipelineNames.PIPELINE_3)
public class Pipeline3RouteBuilder extends BaseFolderWatcherPipeline {

    private final Pipeline3Processor pipeline3Processor;
    @Value("${pipeline3.source.folder}")
    private String sourceFolder;
    @Value("${pipeline3.target.folder}")
    private String targetFolder;

    public Pipeline3RouteBuilder(MdcProcessor mdcProcessor,
                                 StartFileLogger startFileLogger,
                                 ProcessedFileLogger processedFileLogger,
                                 ApplicationContext applicationContext,
                                 ExceptionFileLogger exceptionFileLogger,
                                 Pipeline3Processor pipeline3Processor) {
        super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        this.pipeline3Processor = pipeline3Processor;
    }

    @Override
    public String getPipelineName() {
        return PipelineNames.PIPELINE_3;
    }

    @Override
    public BaseFileProcessor getProcessor() {
        return pipeline3Processor;
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

