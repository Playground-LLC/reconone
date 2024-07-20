package com.company.reconone.common.processors;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that keeps track of the number of records processed and skipped by each stage in a pipeline.
 */
public class RecordCounter {
    private final Map<String, Integer> recordsProcessed = new HashMap<>();
    private final Map<String, Integer> recordsSkipped = new HashMap<>();

    /**
     * Increments the count of processed records for a given stage.
     *
     * @param stage the stage name
     */
    public synchronized void incrementProcessed(String stage) {
        recordsProcessed.put(stage, getRecordsProcessed(stage) + 1);
    }

    /**
     * Increments the count of skipped records for a given stage.
     *
     * @param stage the stage name
     */
    public synchronized void incrementSkipped(String stage) {
        recordsSkipped.put(stage, getRecordsSkipped(stage) + 1);
    }

    /**
     * Gets the count of processed records for a given stage.
     *
     * @param stage the stage name
     * @return the count of processed records
     */
    public synchronized int getRecordsProcessed(String stage) {
        return recordsProcessed.getOrDefault(stage, 0);
    }

    /**
     * Gets the count of skipped records for a given stage.
     *
     * @param stage the stage name
     * @return the count of skipped records
     */
    public synchronized int getRecordsSkipped(String stage) {
        return recordsSkipped.getOrDefault(stage, 0);
    }

    /**
     * Gets a map of all stages and their respective counts of processed records.
     *
     * @return a map of all processed records
     */
    public synchronized Map<String, Integer> getAllRecordsProcessed() {
        return new HashMap<>(recordsProcessed);
    }

    /**
     * Gets a map of all stages and their respective counts of skipped records.
     *
     * @return a map of all skipped records
     */
    public synchronized Map<String, Integer> getAllRecordsSkipped() {
        return new HashMap<>(recordsSkipped);
    }

    /**
     * Resets the counters for processed and skipped records.
     */
    public synchronized void reset() {
        recordsProcessed.clear();
        recordsSkipped.clear();
    }
}
