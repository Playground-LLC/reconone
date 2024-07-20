package com.company.reconone.repository;

import com.company.reconone.domain.ProcessedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDataRepository extends JpaRepository<ProcessedData, Long> {
}
