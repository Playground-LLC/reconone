package com.company.reconone.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ETLKafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.processedData}")
    private String topic;

    public void sendToKafka(String data) {
        kafkaTemplate.send(topic, data);
    }
}
