package com.company.reconone.common.processors;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that keeps track of the number of records processed and skipped by each step in a pipeline.
 */
public class RecordCounter {
    private final Map<String, Integer> recordsProcessed = new HashMap<>();
    private final Map<String, Integer> recordsSkipped = new HashMap<>();

    public synchronized void incrementProcessed(String step) {
        recordsProcessed.put(step, getRecordsProcessed(step) + 1);
    }

    public synchronized void incrementSkipped(String step) {
        recordsSkipped.put(step, getRecordsSkipped(step) + 1);
    }

    public synchronized int getRecordsProcessed(String step) {
        return recordsProcessed.getOrDefault(step, 0);
    }

    public synchronized int getRecordsSkipped(String step) {
        return recordsSkipped.getOrDefault(step, 0);
    }

    public synchronized Map<String, Integer> getAllRecordsProcessed() {
        return new HashMap<>(recordsProcessed);
    }

    public synchronized Map<String, Integer> getAllRecordsSkipped() {
        return new HashMap<>(recordsSkipped);
    }

    public synchronized void reset() {
        recordsProcessed.clear();
        recordsSkipped.clear();
    }
}
