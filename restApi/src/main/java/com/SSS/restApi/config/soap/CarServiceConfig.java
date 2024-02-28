package com.SSS.restApi.config.soap;

import com.SSS.restApi.controllers.soap.CarServiceImpl;
import com.SSS.restApi.controllers.soap.MotoServiceImpl;
import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.services.soap.CarServiceForKafka;
import com.SSS.restApi.services.soap.MotoServiceForKafka;
import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Configuration
public class CarServiceConfig {

    private final Bus bus;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CarServiceForKafka carServiceForKafka;
    private final MotoServiceForKafka motoServiceForKafka;

    @Bean
    public Endpoint carEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CarServiceImpl(kafkaTemplate, carServiceForKafka));
        endpoint.publish("/CarService");
        return endpoint;
    }

    @Bean
    public Endpoint motoEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, new MotoServiceImpl(kafkaTemplate, motoServiceForKafka));
        endpoint.publish("/MotoService");
        return endpoint;
    }

}
