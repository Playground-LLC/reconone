package com.company.reconone.common.processors;

import org.apache.camel.Exchange;

/**
 * Base class for file processors.
 * <p>
 * This class provides a template for processing files. Subclasses must implement the processFile() method to define how files are processed.
 * The process() method is the main entry point for processing files. It initializes the record counter and calls the processFile() method to process the file.
 */
public abstract class BaseFileProcessor {

    private RecordCounter recordCounter;

    /**
     * Abstract method to be implemented by subclasses to define file processing logic.
     *
     * @param exchange the Camel exchange containing the file data
     */
    public abstract void processFile(Exchange exchange);

    /**
     * Main entry point for processing files.
     * Initializes the record counter and calls the processFile() method.
     *
     * @param exchange the Camel exchange containing the file data
     */
    public void process(Exchange exchange) {
        initRecordCounter(exchange);
        processFile(exchange);
    }

    /**
     * Initializes the record counter from the exchange properties.
     *
     * @param exchange the Camel exchange containing the file data
     * @return the initialized RecordCounter
     */
    protected RecordCounter initRecordCounter(Exchange exchange) {
        recordCounter = exchange.getProperty("recordCounter", RecordCounter.class);
        return recordCounter;
    }

    /**
     * Gets the current record counter.
     *
     * @return the current RecordCounter
     */
    protected RecordCounter getRecordCounter() {
        return recordCounter;
    }

    /**
     * Increments the count of processed records for a given step.
     *
     * @param step the processing step
     */
    protected void incrementProcessed(String step) {
        getRecordCounter().incrementProcessed(step);
    }

    /**
     * Increments the count of skipped records for a given step.
     *
     * @param step the processing step
     */
    protected void incrementSkipped(String step) {
        getRecordCounter().incrementSkipped(step);
    }
}
