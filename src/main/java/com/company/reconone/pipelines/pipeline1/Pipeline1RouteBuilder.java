package com.company.reconone.pipelines.pipeline1;

import com.company.reconone.common.pipeline.BaseFolderWatcherPipeline;
import com.company.reconone.common.processors.ExceptionFileLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedFileLogger;
import com.company.reconone.common.processors.StartFileLogger;
import com.company.reconone.pipelines.PipelineNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component(PipelineNames.PIPELINE_1)
public class Pipeline1RouteBuilder extends BaseFolderWatcherPipeline {

    private final Pipeline1Processor pipeline1Processor;
    @Value("${pipeline1.source.folder}")
    private String sourceFolder;
    @Value("${pipeline1.target.folder}")
    private String targetFolder;

    public Pipeline1RouteBuilder(MdcProcessor mdcProcessor,
                                 StartFileLogger startFileLogger,
                                 ProcessedFileLogger processedFileLogger,
                                 ApplicationContext applicationContext,
                                 ExceptionFileLogger exceptionFileLogger,
                                 Pipeline1Processor pipeline1Processor) {
        super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        this.pipeline1Processor = pipeline1Processor;
    }

    @Override
    public String getPipelineName() {
        return PipelineNames.PIPELINE_1;
    }

    @Override
    public Object getProcessor() {
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
    public String fileExtension() {
        return "txt";
    }
}
