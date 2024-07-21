package com.company.reconone.pipelines.pipeline3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Pipeline3Constants {
    @Getter
    @RequiredArgsConstructor
    public enum Stages {
        STAGE_1_MARIADB("saveToDatabase"),
        STAGE_2_MONGODB("saveToMongo"),
        STAGE_3_KAFKA("sendToKafka");

        private final String stage;
    }
}
