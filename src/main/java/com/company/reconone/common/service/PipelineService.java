package com.company.reconone.common.service;

import com.company.reconone.common.domain.*;
import com.company.reconone.common.repository.FileProcessingRepository;
import com.company.reconone.common.repository.MessageProcessingRepository;
import com.company.reconone.common.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final PipelineRepository pipelineRepository;
    private final FileProcessingRepository fileProcessingRepository;
    private final MessageProcessingRepository messageProcessingRepository;

    public Optional<Pipeline> findPipelineById(PipelineId pipelineId) {
        return pipelineRepository.findById(pipelineId);
    }

    public Pipeline savePipeline(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public List<FileProcessingInfo> findFileProcessingByPipelineId(String pipelineId) {
        return fileProcessingRepository.findAllByPipelineId(pipelineId);
    }

    public List<MessageProcessingInfo> findMessageProcessingByPipelineId(String pipelineId) {
        return messageProcessingRepository.findAllByPipelineId(pipelineId);
    }
}
