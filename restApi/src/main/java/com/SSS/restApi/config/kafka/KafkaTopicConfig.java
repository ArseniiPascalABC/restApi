package com.SSS.restApi.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
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
                .name("soapTopic")
                .replicas(1)
                .partitions(1)
                .build();
    }

}
