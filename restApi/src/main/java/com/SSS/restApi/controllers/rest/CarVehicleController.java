package com.sss.restapi.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sss.restapi.controllers.soap.CarService;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.responses.soap.CarResponse;
import com.sss.restapi.services.rest.CarVehicleService;
import com.sss.restapi.xmlwrapper.rest.CarListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
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

    @Operation(summary = "Get car by id", description = "Get car found by RequestParam id")
    @ApiResponse(responseCode = "200", description = "Vehicles by id was found",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
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

    @Operation(summary = "Get car by id using soap protocol", description = "Get car found by RequestParam id using soap protocol")
    @ApiResponse(responseCode = "200", description = "Vehicles by brand was found using soapRequest",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
    @GetMapping(value = "/soapRequest", produces = MediaType.APPLICATION_XML_VALUE)
    public CarResponse soapRequestGetVehicleById(Long id)
            throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        String wsdlUrl = "http://localhost:8080/Service/CarService?wsdl";
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CarService.class);
        factory.setAddress(wsdlUrl);
        CarService carService = (CarService) factory.create();

        return carService.getVehicleById(id);
    }

    @Operation(summary = "Get cars by brand", description = "Get cars found by RequestParam brand")
    @ApiResponse(responseCode = "200", description = "Vehicles by brand was found",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = CarListResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
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

    @Operation(summary = "Add a new vehicle", description = "Add a new vehicle to the system")
    @ApiResponse(responseCode = "200", description = "Vehicle added successfully",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ResponseEntity.class)))
    @Override
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addVehicle(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON representation of the vehicle", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Car.class)))
            @RequestBody String requestBody
    ) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "addVehicle");
        jsonMessage.put(BODY, requestBody);
        kafkaTemplate.send(REST_TOPIC, jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }

}
