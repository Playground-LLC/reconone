package com.company.reconone.common.repository;

import com.company.reconone.common.domain.FileProcessingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileProcessingRepository extends JpaRepository<FileProcessingInfo, Long> {

    List<FileProcessingInfo> findAllByPipelineId(String pipelineId);
}
