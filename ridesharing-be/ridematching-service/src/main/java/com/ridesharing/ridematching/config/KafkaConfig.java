package com.ridesharing.ridematching.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private static final String DRIVER_REQUEST_TOPIC = "DriverRequestTopic";

    @Bean
    public NewTopic rideTopic(){
        return TopicBuilder.name(DRIVER_REQUEST_TOPIC).build();
    }
}
