package com.company.reconone.common.controller.model;

public class PipelineStatusResponse {
    private String pipelineId;
    private String status;

    public PipelineStatusResponse(String pipelineId, String status) {
        this.pipelineId = pipelineId;
        this.status = status;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
