package com.SSS.restApi.controllers;

import com.SSS.restApi.models.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public interface VehicleController {
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id);

    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand);

    public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle);

}
