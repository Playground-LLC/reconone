package com.company.reconone.common.repository;

import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.domain.PipelineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineRepository extends JpaRepository<Pipeline, PipelineId> {
    List<Pipeline> findByInstanceId(String instanceId);

    void deleteAllByInstanceId(String instanceId);
}
