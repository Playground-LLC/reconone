package com.company.reconone.pipelines.pipeline1;

import com.company.reconone.common.pipeline.BaseFolderWatcherPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("pipeline1")
public class Pipeline1RouteBuilder extends BaseFolderWatcherPipeline {

    private static final Logger logger = LoggerFactory.getLogger(Pipeline1RouteBuilder.class);

    @Value("${pipeline1.source.folder}")
    private String sourceFolder;

    @Value("${pipeline1.target.folder}")
    private String targetFolder;

    @Autowired
    private Pipeline1Processor pipeline1Processor;

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
