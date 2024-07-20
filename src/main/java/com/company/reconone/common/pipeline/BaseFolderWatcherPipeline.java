package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedFileLogger;
import com.company.reconone.common.processors.StartFileLogger;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Base class for all folder watcher pipelines.
 * <p>
 * This class is responsible for configuring the Camel route that watches a folder for new files, processes them, and moves them to a different folder.
 * <p>
 * The route is configured to watch the folder specified by the sourceFolder() method.
 * When a new file is detected, the route will process the file using the processor specified by the getProcessor() method.
 * The route will then move the file to the folder specified by the destinationFolder() method.
 * <p>
 * The route is also configured to add the pipelineId to the MDC context using the MdcProcessor, log the start and end of the file processing using the StartFileLogger and ProcessedFileLogger processors.
 * <p>
 * The sourceFolder(), destinationFolder(), and fileExtension() methods must be implemented by subclasses to specify the source folder, destination folder, and file extension for the pipeline.
 *
 * The pipeline can also implement configure() optionally to add additional route configuration.
 */
@Component
public abstract class BaseFolderWatcherPipeline extends RouteBuilder {

    private static final long DELAY_READ_FILE = 5000;

    @Value("${instance.id}")
    protected String instanceId;
    @Autowired
    protected MdcProcessor mdcProcessor;
    @Autowired
    protected StartFileLogger startFileLogger;
    @Autowired
    protected ProcessedFileLogger processedFileLogger;
    @Autowired
    private ApplicationContext applicationContext;

    abstract public String getPipelineName();
    abstract public Object getProcessor();
    abstract public String sourceFolder();

    public String destinationFolder() { return "processed"; }
    public String errorFolder() { return "error"; }
    public String fileExtension() { return null; }
    public String splitter() { return "\n"; }
    public int chunkSize() { return 0; }


    /**
     * Configures the Camel route that watches a folder for new files, processes them, and moves them to a different folder.
     */
    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .log("Error processing file: ${header.CamelFileName}")
                .to("file:" + errorFolder() + "?fileName=${header.CamelFileName}");

        if (chunkSize() > 0) {
            String beanName = applicationContext.getBeanNamesForType(getProcessor().getClass())[0];
            from(constructFromRoute())
                    .routeId(getPipelineName())
                    .process(startFileLogger)
                    .process(mdcProcessor)
                    .log("Processing file: ${header.CamelFileName}")
                    .split(body().tokenize(splitter(), chunkSize(), false), new FileChunkAggregator())
                    .to("bean:" + beanName + "?method=process")
                    .end()
                    .process(processedFileLogger)
                    .end();
        } else {
            from(constructFromRoute())
                    .routeId(getPipelineName())
                    .process(startFileLogger)
                    .process(mdcProcessor)
                    .log("Processing file: ${header.CamelFileName}")
                    .bean(getProcessor(), "process")
                    .process(processedFileLogger)
                    .end();
        }
    }

    String constructFromRoute() {
        String route = "file:" + sourceFolder() + "?delay=" + DELAY_READ_FILE + "&sortBy=file:modified";

        if (destinationFolder() != null && !destinationFolder().isBlank()) {
            route += "&move=" + destinationFolder() + "/${file:name}";
        }

        if (fileExtension() != null && !fileExtension().isBlank()) {
            route += "&include=.*\\." + fileExtension();
        }

        return route;
    }

    public static class FileChunkAggregator implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            return oldExchange;
        }
    }


}
