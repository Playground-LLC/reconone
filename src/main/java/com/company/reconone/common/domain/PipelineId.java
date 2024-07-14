package com.company.reconone.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a pipeline id in the database.
 */
public class PipelineId implements Serializable {

    private String pipelineId;
    private String instanceId;

    public PipelineId() {
    }

    public PipelineId(String pipelineId, String instanceId) {
        this.pipelineId = pipelineId;
        this.instanceId = instanceId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipelineId that = (PipelineId) o;
        return Objects.equals(pipelineId, that.pipelineId) &&
                Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pipelineId, instanceId);
    }
}
