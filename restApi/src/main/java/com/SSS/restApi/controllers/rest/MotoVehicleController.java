package com.sss.restapi.controllers.rest;

import com.sss.restapi.services.rest.MotoVehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest/motos")
public class MotoVehicleController implements VehicleController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MotoVehicleService motoVehicleService;

    private static final String VEHICLE = "Vehicle";
    private static final String METHOD = "Method";
    private static final String BODY = "Body";
    private static final String REST_TOPIC = "restTopic";

    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "getVehicleById");
        jsonMessage.put(BODY, id.toString());
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "getVehiclesByBrand");
        jsonMessage.put(BODY, brand);
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addVehicle(@RequestBody String requestBody) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "addVehicle");
        jsonMessage.put(BODY, requestBody);
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

}
