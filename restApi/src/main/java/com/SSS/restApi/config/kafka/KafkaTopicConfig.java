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
    public NewTopic soapCarTopic(){
        return TopicBuilder
                .name("soapCarTopic")
                .replicas(1)
                .partitions(1)
                .build();
    }

    @Bean
    public NewTopic soapMotoTopic(){
        return TopicBuilder
                .name("soapMotoTopic")
                .replicas(1)
                .partitions(1)
                .build();
    }

}
