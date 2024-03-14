package com.sss.restapi.controllers.rest;

import com.sss.restapi.controllers.soap.CarService;
import com.sss.restapi.responses.soap.CarResponse;
import com.sss.restapi.services.rest.CarVehicleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest/cars")
public class CarVehicleController implements VehicleController {
    private static final String VEHICLE = "Vehicle";
    private static final String METHOD = "Method";
    private static final String BODY = "Body";
    private static final String REST_TOPIC = "restTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CarVehicleService carVehicleService;


    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "getVehicleById");
        jsonMessage.put(BODY, id.toString());
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

    @GetMapping(value = "/soapRequest", produces = MediaType.APPLICATION_XML_VALUE)
    public CarResponse soapRequestGetVehicleById(Long id) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        String wsdlUrl = "http://localhost:8080/Service/CarService?wsdl";
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CarService.class);
        factory.setAddress(wsdlUrl);
        CarService carService = (CarService) factory.create();
        return carService.getVehicleById(id);
    }

    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "getVehiclesByBrand");
        jsonMessage.put(BODY, brand);
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addVehicle(@RequestBody String requestBody) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "addVehicle");
        jsonMessage.put(BODY, requestBody);
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());

    }

}
