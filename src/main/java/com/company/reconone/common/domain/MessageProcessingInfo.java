package com.company.reconone.common.domain;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class MessageProcessingInfo implements ProcessingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messageId;
    private Long startTime;
    private Long endTime;
    private Long timeTaken;
    private String status;

    @Lob
    private String errorStackTrace;

    @ElementCollection
    private Map<String, Integer> recordsProcessed;

    @ElementCollection
    private Map<String, Integer> recordsSkipped;

    private String pipelineId;
    private String instanceId;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public Long getStartTime() {
        return startTime;
    }

    @Override
    public Long getEndTime() {
        return endTime;
    }

    @Override
    public Long getTimeTaken() {
        return timeTaken;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    @Override
    public Map<String, Integer> getRecordsProcessed() {
        return recordsProcessed;
    }

    @Override
    public Map<String, Integer> getRecordsSkipped() {
        return recordsSkipped;
    }

    @Override
    public String getPipelineId() {
        return pipelineId;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }

    public void setRecordsProcessed(Map<String, Integer> recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public void setRecordsSkipped(Map<String, Integer> recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }


    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
