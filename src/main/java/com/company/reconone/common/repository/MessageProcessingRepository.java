package com.company.reconone.common.repository;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.domain.MessageProcessingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageProcessingRepository extends JpaRepository<MessageProcessingInfo, Long> {

    List<MessageProcessingInfo> findAllByPipelineId(String pipelineId);
}
