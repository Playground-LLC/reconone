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
@EqualsAndHashCode
@ToString
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

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> recordsProcessed;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> recordsSkipped;

    @Lob
    private String errorStackTrace;

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