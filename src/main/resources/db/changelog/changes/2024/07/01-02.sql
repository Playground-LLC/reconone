CREATE TABLE IF NOT EXISTS file_processing_info
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name   VARCHAR(512) NOT NULL,
    file_size   BIGINT NULL DEFAULT NULL,
    start_time  BIGINT NULL DEFAULT NULL,
    end_time    BIGINT NULL DEFAULT NULL,
    time_taken  BIGINT NULL DEFAULT NULL,
    status      VARCHAR(50) NULL DEFAULT NULL,
    error_stack_trace LONGTEXT NULL DEFAULT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    pipeline_id VARCHAR(255),
    instance_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS file_processing_info_records_processed
(
    file_processing_info_id BIGINT,
    records_processed_key   VARCHAR(512) NULL DEFAULT NULL,
    records_processed       INT NULL DEFAULT NULL,
    PRIMARY KEY (file_processing_info_id, records_processed_key),
    FOREIGN KEY (file_processing_info_id) REFERENCES file_processing_info (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS file_processing_info_records_skipped
(
    file_processing_info_id BIGINT,
    records_skipped_key     VARCHAR(512) NULL DEFAULT NULL,
    records_skipped         INT NULL DEFAULT NULL,
    PRIMARY KEY (file_processing_info_id, records_skipped_key),
    FOREIGN KEY (file_processing_info_id) REFERENCES file_processing_info (id) ON DELETE CASCADE
);

