package com.company.reconone.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/**
 * Represents a pipeline in the database.
 */
@Entity
@IdClass(PipelineId.class)
public class Pipeline {

    @Id
    private String pipelineId;

    @Id
    private String instanceId;

    private String status;

    public Pipeline() {
    }

    public Pipeline(String pipelineId, String instanceId, String status) {
        this.pipelineId = pipelineId;
        this.instanceId = instanceId;
        this.status = status;
    }

    // Getters and setters
    public String getPipelineId() {
        return pipelineId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
