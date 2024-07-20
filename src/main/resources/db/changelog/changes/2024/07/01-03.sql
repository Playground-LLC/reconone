CREATE TABLE message_processing_info
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id        VARCHAR(255) NULL DEFAULT NULL,
    start_time        BIGINT NULL DEFAULT NULL,
    end_time          BIGINT NULL DEFAULT NULL,
    time_taken        BIGINT NULL DEFAULT NULL,
    status            VARCHAR(50) NULL DEFAULT NULL,
    error_stack_trace TEXT NULL DEFAULT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    pipeline_id       VARCHAR(255),
    instance_id       VARCHAR(255)
);

CREATE TABLE message_processing_records_processed
(
    message_processing_info_id BIGINT,
    stage_name                 VARCHAR(255) NULL DEFAULT NULL,
    count                      INT NULL DEFAULT NULL,
    PRIMARY KEY (message_processing_info_id, stage_name),
    FOREIGN KEY (message_processing_info_id) REFERENCES message_processing_info (id) ON DELETE CASCADE
);

CREATE TABLE message_processing_records_skipped
(
    message_processing_info_id BIGINT,
    stage_name                 VARCHAR(255) NULL DEFAULT NULL,
    count                      INT NULL DEFAULT NULL,
    PRIMARY KEY (message_processing_info_id, stage_name),
    FOREIGN KEY (message_processing_info_id) REFERENCES message_processing_info (id) ON DELETE CASCADE
);
