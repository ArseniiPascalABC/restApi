package com.sss.restapi.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public interface VehicleController {
    ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id);

    ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand);

    ResponseEntity<Object> addVehicle(@RequestBody String requestBody);

}
