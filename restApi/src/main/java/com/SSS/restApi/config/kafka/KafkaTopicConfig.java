package com.sss.restapi.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.soap.topic.name}")
    private String soapTopicName;
    @Bean
    public NewTopic restTopic(){
        return TopicBuilder
                .name("restTopic")
                .replicas(1)
                .partitions(1)
                .build();
    }
    @Bean
    public NewTopic soapTopic(){
        return TopicBuilder
                .name(soapTopicName)
                .replicas(1)
                .partitions(1)
                .build();
    }


}
