package com.company.reconone.common.domain;

import java.util.Map;

public interface ProcessingInfo {
    String getPipelineId();
    Long getStartTime();
    Long getEndTime();
    Long getTimeTaken();
    String getStatus();
    String getErrorStackTrace();
    Map<String, Integer> getRecordsProcessed();
    Map<String, Integer> getRecordsSkipped();
}
