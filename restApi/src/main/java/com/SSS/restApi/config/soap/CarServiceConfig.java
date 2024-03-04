package com.SSS.restApi.config.soap;

import com.SSS.restApi.controllers.soap.CarServiceImpl;
import com.SSS.restApi.controllers.soap.MotoServiceImpl;
import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class CarServiceConfig {

    private final Bus bus;

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Bean
    public Endpoint carEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CarServiceImpl(replyingKafkaTemplate));
        endpoint.publish("/CarService");
        return endpoint;
    }

    @Bean
    public Endpoint motoEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, new MotoServiceImpl(replyingKafkaTemplate));
        endpoint.publish("/MotoService");
        return endpoint;
    }

}
