package com.company.reconone.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Represents a pipeline id in the database.
 */
@Data
@NoArgsConstructor
public class PipelineId implements Serializable {

    private String pipelineId;
    private String instanceId;

    public PipelineId(String pipelineId, String instanceId) {
        this.pipelineId = pipelineId;
        this.instanceId = instanceId;
    }
}
