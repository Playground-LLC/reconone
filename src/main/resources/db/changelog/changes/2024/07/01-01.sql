CREATE TABLE pipeline (
    pipeline_id VARCHAR(255),
    instance_id VARCHAR(255),
    status VARCHAR(10) NOT NULL,
    PRIMARY KEY (pipeline_id, instance_id)
);
