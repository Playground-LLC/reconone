package com.company.reconone.common.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a pipeline in the database.
 */
@Entity
@IdClass(PipelineId.class)
@Data
@NoArgsConstructor
public class Pipeline {

    @Id
    private String pipelineId;

    @Id
    private String instanceId;

    private String status;

    public Pipeline(String pipelineId, String instanceId, String status) {
        this.pipelineId = pipelineId;
        this.instanceId = instanceId;
        this.status = status;
    }
}
