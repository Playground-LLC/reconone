package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.ExceptionFileLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedFileLogger;
import com.company.reconone.common.processors.StartFileLogger;
import lombok.RequiredArgsConstructor;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
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
 * <p>
 * The pipeline can also implement configure() optionally to add additional route configuration.
 */
@Component
@RequiredArgsConstructor
public abstract class BaseFolderWatcherPipeline extends RouteBuilder {

    private static final long DELAY_READ_FILE = 5000; // Delay between file reads
    protected final MdcProcessor mdcProcessor;
    protected final StartFileLogger startFileLogger;
    protected final ProcessedFileLogger processedFileLogger;
    protected final ApplicationContext applicationContext;
    protected final ExceptionFileLogger exceptionFileLogger;

    @Value("${instance.id}")
    protected String instanceId;

    /**
     * Get the name of the pipeline.
     *
     * @return pipeline name
     */
    abstract public String getPipelineName();

    /**
     * Get the processor to handle file processing.
     *
     * @return processor instance
     */
    abstract public Object getProcessor();

    /**
     * Get the source folder to watch for new files.
     *
     * @return source folder path
     */
    abstract public String sourceFolder();

    /**
     * Get the folder for files in progress.
     *
     * @return in-progress folder path
     */
    public String processingFolder() {
        return "inprogress";
    }

    /**
     * Get the destination folder for processed files.
     *
     * @return destination folder path
     */
    public String destinationFolder() {
        return "processed";
    }

    /**
     * Get the folder for files with errors.
     *
     * @return error folder path
     */
    public String errorFolder() {
        return "error";
    }

    /**
     * Get the file extension to watch for.
     *
     * @return file extension
     */
    public String fileExtension() {
        return null;
    }

    /**
     * Get the splitter for chunking files.
     *
     * @return splitter string
     */
    public String splitter() {
        return "\n";
    }

    /**
     * Get the chunk size for splitting files.
     *
     * @return chunk size
     */
    public int chunkSize() {
        return 0;
    }

    /**
     * Configure the Camel route to watch, process, and move files.
     */
    @Override
    public void configure() {
        configureExceptionHandling();
        configureRoute();
    }

    /**
     * Configure exception handling to move files to the error folder on failure.
     */
    void configureExceptionHandling() {
        onException(Exception.class)
                .handled(true)
                .log("Error processing file: ${header.CamelFileName}")
                .log(LoggingLevel.ERROR, "Stacktrace: ${exception.stacktrace}")
                .process(exceptionFileLogger)
                .to(buildErrorFolderUri());
    }

    /**
     * Configure the route to watch a folder, process files, and move them to the destination folder.
     */
    private void configureRoute() {
        if (chunkSize() > 0) {
            configureChunkedRoute();
        } else {
            configureSimpleRoute();
        }
    }

    /**
     * Configure a route for chunked file processing.
     */
    private void configureChunkedRoute() {
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
                .to(buildDestinationFolderUri())
                .end();
    }

    /**
     * Configure a simple route for file processing.
     */
    private void configureSimpleRoute() {
        from(constructFromRoute())
                .routeId(getPipelineName())
                .process(startFileLogger)
                .process(mdcProcessor)
                .log("Processing file: ${header.CamelFileName}")
                .bean(getProcessor(), "process")
                .process(processedFileLogger)
                .to(buildDestinationFolderUri());
    }

    /**
     * Construct the URI for the route's "from" endpoint.
     *
     * @return constructed URI
     */
    String constructFromRoute() {
        StringBuilder route = new StringBuilder("file:" + sourceFolder() + "?delay=" + DELAY_READ_FILE + "&sortBy=file:modified&delete=true");
        if (processingFolder() != null && !processingFolder().isBlank()) {
            route.append("&preMove=").append(processingFolder());
        }
        if (fileExtension() != null && !fileExtension().isBlank()) {
            route.append("&include=.*\\.").append(fileExtension());
        }
        return route.toString();
    }

    /**
     * Build the URI for the destination folder.
     *
     * @return constructed URI
     */
    String buildDestinationFolderUri() {
        return "file:" + destinationFolder()
                + "?delay=3000&fileExist=Move&moveExisting=${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}";
    }

    /**
     * Build the URI for the error folder.
     *
     * @return constructed URI
     */
    String buildErrorFolderUri() {
        return "file:" + errorFolder()
                + "?delay=3000&fileExist=Move&moveExisting=${file:name.noext}-${date:now:yyyyMMddHHmmssSSS}.${file:ext}";
    }

    /**
     * Aggregator class for file chunk processing.
     */
    public static class FileChunkAggregator implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            return oldExchange;
        }
    }
}