package com.SSS.restApi.controllers.rest;

import com.SSS.restApi.models.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public interface VehicleController {
    ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id);

    ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand);

    ResponseEntity<String> addVehicle(@RequestBody String requestBody);

}
