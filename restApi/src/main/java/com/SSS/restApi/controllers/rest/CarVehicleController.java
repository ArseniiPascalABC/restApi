package com.SSS.restApi.controllers.rest;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.models.Vehicle;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.xmlWrapper.CarListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/rest/cars")
public class CarVehicleController implements VehicleController {

    private final CarRepository carRepository;
    private final CarDAO carDAO;
    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        Vehicle vehicle = carRepository.findById(id).orElse(null);
        if (vehicle == null) {
            log.warn("CarController getVehicleById, the vehicle the user was looking for was not found in database cars");
            return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + id + " в базе данных cars</message>");
        }
        log.info("CarController getVehicleById, the vehicle the user was looking for was found in database cars");
        return ResponseEntity.ok(vehicle);
    }

    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        List<Car> cars = carRepository.findAllByBrandIgnoreCase(brand);
        if (cars.isEmpty()) {
            log.warn("CarController getCarsByBrand, the cars the user was looking for were not found in database cars");
            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + brand + " в базе данных cars</message>");
        }
        CarListResponse carListResponse = new CarListResponse(cars);
        log.info("CarController getCarsByBrand, the cars the user was looking for were found");
        return ResponseEntity.ok(carListResponse);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> addVehicle(@RequestBody String requestBody) {
        try {
            Vehicle vehicle;
            ObjectMapper mapper = new ObjectMapper();
            vehicle = mapper.readValue(requestBody, Car.class);

            carDAO.save(vehicle);
            log.info("CarController addCar, car was added");
            return ResponseEntity.status(HttpStatus.CREATED).body("<message>Запись добавлена в auto</message>");
        } catch (Exception e) {
            log.info("CarController addCar, car was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Запись не была добавлена в auto</message>");
        }
    }

}
