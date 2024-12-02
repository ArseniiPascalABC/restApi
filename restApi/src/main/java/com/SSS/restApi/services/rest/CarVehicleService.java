package com.sss.restapi.services.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.restapi.dao.CarDAO;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.repositories.car.CarRepository;
import com.sss.restapi.xmlwrapper.rest.CarListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CarVehicleService implements VehicleService {
    private static final String UNSUPPORTED_VEHICLE = "Unsupported vehicle";

    private final CarRepository carRepository;

    private final CarDAO carDAO;

    @Override
    public ResponseEntity<Object> processMessageAndGetResponse(String message) {
        String method;
        String vehicle;
        String body;
        try {
            JSONObject jsonMessage = new JSONObject(message);
            method = jsonMessage.getString("Method");
            vehicle = jsonMessage.getString("Vehicle");
            body = jsonMessage.getString("Body");
        } catch (JSONException e) {
            log.warn("Исключение {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message");
        }
        if ("Car".equals(vehicle)) {
            switch (method) {
                case "getVehicleById" -> {
                    Car car = carRepository.findById(Long.parseLong(body)).orElse(null);
                    if (car == null) {
                        log.warn(
                                "CarController getVehicleById, the vehicle the user was looking for(in dataBase cars) was not found");
                        return ResponseEntity
                                .ok()
                                .body("<message>Ничего не найдено по id = " + body + " в базе данных cars</message>");
                    } else {
                        log.info(
                                "CarController getVehicleById, the vehicle the user was looking for was found in database cars");
                        return ResponseEntity.ok(car);
                    }
                }
                case "getVehiclesByBrand" -> {
                    List<Car> cars = carRepository.findAllByBrandIgnoreCase(body);
                    if (cars.isEmpty()) {
                        log.warn(
                                "CarController getVehiclesByBrand, the vehicles the user was looking for were not found in database cars");
                        return ResponseEntity
                                .ok()
                                .body("<message>Ничего не найдено по бренду " + body + " в базе данных cars</message>");
                    }
                    CarListResponse carListResponse = new CarListResponse(cars);
                    log.info("CarController getVehiclesByBrand, the vehicles the user was looking for were found");
                    return ResponseEntity.ok(carListResponse);
                }
                case "addVehicle" -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Car car = mapper.readValue(body, Car.class);
                        carDAO.save(car);
                        log.info("CarController addVehicle, Car was added");
                        return ResponseEntity.status(HttpStatus.CREATED).body("<message>Автомобиль добавлен</message>");
                    } catch (Exception e) {
                        log.error("CarController addVehicle, Car was not added");
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("<message>Автомобиль не был добавлен</message>");
                    }
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UNSUPPORTED_VEHICLE);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UNSUPPORTED_VEHICLE);
    }
}
