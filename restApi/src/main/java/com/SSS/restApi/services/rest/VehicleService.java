package com.SSS.restApi.services.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface VehicleService {
    ResponseEntity<Object> processMessageAndGetResponse(String message);

}
