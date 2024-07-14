package com.company.reconone.common.processors;

import org.apache.camel.Exchange;

/**
 * Base class for file processors.
 *
 * This class provides a template for processing files. Subclasses must implement the processFile() method to define how files are processed.
 *
 * The process() method is the main entry point for processing files. It initializes the record counter and calls the processFile() method to process the file.
 */
public abstract class BaseFileProcessor {
    private RecordCounter recordCounter;

    public abstract void processFile(Exchange exchange);

    public void process(Exchange exchange) {
        initRecordCounter(exchange);
        processFile(exchange);
    }

    protected RecordCounter initRecordCounter(Exchange exchange){
        recordCounter = exchange.getProperty("recordCounter", RecordCounter.class);
        return recordCounter;
    }

    protected RecordCounter getRecordCounter(){
        return recordCounter;
    }

    protected void incrementProcessed(String step) {
        getRecordCounter().incrementProcessed(step);
    }

    protected void incrementSkipped(String step) {
        getRecordCounter().incrementSkipped(step);
    }
}

