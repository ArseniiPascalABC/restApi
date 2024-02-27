package com.SSS.restApi.controllers.rest;

import com.SSS.restApi.services.rest.MotoVehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest/motos")
public class MotoVehicleController implements VehicleController{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MotoVehicleService motoVehicleService;

    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        System.out.println(jsonMessage);
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        System.out.println(jsonMessage);
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());

    }

    @Override
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addVehicle(@RequestBody String requestBody) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "addVehicle");
        jsonMessage.put("Body", requestBody);
        System.out.println(jsonMessage);
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return motoVehicleService.processMessageAndGetResponse(jsonMessage.toString());

    }

}
