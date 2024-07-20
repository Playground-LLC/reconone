package com.company.reconone.common.domain;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class FileProcessingInfo implements ProcessingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private Long fileSize;
    private Long startTime;
    private Long endTime;
    private Long timeTaken;
    private String status;

    @ElementCollection
    private Map<String, Integer> recordsProcessed;

    @ElementCollection
    private Map<String, Integer> recordsSkipped;

    @Lob
    private String errorStackTrace;

    private String pipelineId;
    private String instanceId;

    public FileProcessingInfo() {
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
    public Map<String, Integer> getRecordsProcessed() {
        return recordsProcessed;
    }


    @Override
    public Map<String, Integer> getRecordsSkipped() {
        return recordsSkipped;
    }

    @Override
    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    @Override
    public String getPipelineId() {
        return pipelineId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
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

    public void setRecordsProcessed(Map<String, Integer> recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public void setRecordsSkipped(Map<String, Integer> recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
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
