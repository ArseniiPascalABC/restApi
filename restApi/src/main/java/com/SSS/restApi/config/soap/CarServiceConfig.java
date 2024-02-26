package com.SSS.restApi.config.soap;

import com.SSS.restApi.controllers.soap.CarServiceImpl;
import com.SSS.restApi.controllers.soap.MotoServiceImpl;
import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.repositories.moto.MotoRepository;
import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@RequiredArgsConstructor
@Configuration
public class CarServiceConfig {

    private final Bus bus;
    private final CarRepository carRepository;
    private final CarDAO carDAO;

    private final MotoRepository motoRepository;
    private final MotoDAO motoDAO;

    @Bean
    public Endpoint carEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CarServiceImpl(carRepository, carDAO));
        endpoint.publish("/CarService");
        return endpoint;
    }

    @Bean
    public Endpoint motoEndpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, new MotoServiceImpl(motoRepository, motoDAO));
        endpoint.publish("/MotoService");
        return endpoint;
    }

}
