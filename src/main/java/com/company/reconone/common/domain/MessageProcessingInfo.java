package com.company.reconone.common.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
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

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> recordsProcessed;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> recordsSkipped;

    private String pipelineId;
    private String instanceId;

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
        this.timeTaken = calculateTimeTaken();
    }

    private Long calculateTimeTaken() {
        return (this.startTime != null && this.endTime != null) ? this.endTime - this.startTime : null;
    }
}
