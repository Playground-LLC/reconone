package com.company.reconone.service;

import com.company.reconone.domain.ProcessedData;
import com.company.reconone.repository.ProcessedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessedDataService {

    @Autowired
    private ProcessedDataRepository processedDataRepository;

    public void saveProcessedData(String processedData) {
        processedDataRepository.save(new ProcessedData(processedData));
    }
}
