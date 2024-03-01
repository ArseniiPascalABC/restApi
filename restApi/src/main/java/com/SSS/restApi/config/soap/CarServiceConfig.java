package com.SSS.restApi.config.soap;

import com.SSS.restApi.controllers.soap.CarServiceImpl;
import com.SSS.restApi.controllers.soap.MotoServiceImpl;
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

    private final KafkaTemplate<String, String> kafkaTemplateForCar;
    private final KafkaTemplate<String, String> kafkaTemplateForMoto;
    private final CarServiceForKafka carServiceForKafka;
    private final MotoServiceForKafka motoServiceForKafka;

    @Bean
    public Endpoint carEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CarServiceImpl(kafkaTemplateForCar, carServiceForKafka));
        endpoint.publish("/CarService");
        return endpoint;
    }

    @Bean
    public Endpoint motoEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, new MotoServiceImpl(kafkaTemplateForMoto, motoServiceForKafka));
        endpoint.publish("/MotoService");
        return endpoint;
    }

}
