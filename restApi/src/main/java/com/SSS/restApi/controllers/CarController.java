package com.SSS.restApi.controllers;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.repositories.CarRepository;
import com.SSS.restApi.models.Car;
import com.SSS.restApi.xmlWrapper.CarListResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarRepository carRepository;
    private final CarDAO carDAO;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getCarById(@RequestParam("id") Long id) {
        Car car = carRepository.findById(id).orElse(null);
        if (car == null) {
            log.info("CarController getCarById, the car the user was looking for was not found");
            return ResponseEntity.ok().body("<message>Ничего не найдено</message>");
        }
        log.info("CarController getCarById, the car the user was looking for was found");
        return ResponseEntity.ok(car);
    }

    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getCarsByBrand(@RequestParam("brand") String brand) {
        List<Car> cars = carRepository.findAllByBrand(brand);
        if (cars.isEmpty()) {
            log.info("CarController getCarsByBrand, the cars the user was looking for were not found");
            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + brand + "</message>");
        }
        CarListResponse carListResponse = new CarListResponse(cars);

        log.info("CarController getCarsByBrand, the cars the user was looking for were found");
        return ResponseEntity.ok(carListResponse);

    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        try {
            carDAO.save(car);
            log.info("CarController addCar, car was added");
            return ResponseEntity.status(HttpStatus.CREATED).body("<message>Запись добавлена</message>");
        } catch (Exception e) {
            log.info("CarController addCar, car was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Запись не была добавлена</message>");
        }
    }

}
