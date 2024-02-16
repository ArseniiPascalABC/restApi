package com.SSS.restApi.controllers;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.Vehicle;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.xmlWrapper.CarListResponse;
import com.SSS.restApi.xmlWrapper.MotoListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class VehicleController {
    private final CarRepository carRepository;
    private final MotoRepository motoRepository;
    private final CarDAO carDAO;
    private final MotoDAO motoDAO;

    @Autowired
    public VehicleController(@Qualifier("carRepository") CarRepository carRepository,
                             @Qualifier("motoRepository") MotoRepository motoRepository,
                             @Qualifier("carDAO") CarDAO carDAO,
                             @Qualifier("motoDAO") MotoDAO motoDAO
    ) {
        this.carRepository = carRepository;
        this.motoRepository = motoRepository;
        this.carDAO = carDAO;
        this.motoDAO = motoDAO;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id, @RequestParam(value = "databaseName", required = false, defaultValue = "auto") Optional<String> databaseName) {
        String dbNameForMessageAndLogger = "auto";
        Vehicle vehicle = carRepository.findById(id).orElse(null);
        if (databaseName.isPresent() && databaseName.get().equals("moto")) {
            dbNameForMessageAndLogger = "moto";
            vehicle = motoRepository.findById(id).orElse(null);
        }

        if (vehicle == null) {
            log.warn("CarController getVehicleById, the vehicle the user was looking for(in dataBase " + dbNameForMessageAndLogger + ") was not found");
            return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + id + " в базе данных " + dbNameForMessageAndLogger + "</message>");
        }
        log.info("CarController getVehicleById, the vehicle the user was looking for was found in database " + dbNameForMessageAndLogger);
        return ResponseEntity.ok(vehicle);

    }

    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getCarsByBrand(@RequestParam("brand") String brand, @RequestParam(value = "databaseName", required = false, defaultValue = "auto") Optional<String> databaseName) {
        String dbNameForMessageAndLogger = "auto";
        if (databaseName.isPresent() && databaseName.get().equals("moto")) {
            List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(brand);
            dbNameForMessageAndLogger = "moto";
            if (motos.isEmpty()) {
                log.warn("CarController getCarsByBrand, the motos the user was looking for were not found in database " + dbNameForMessageAndLogger);
                return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + brand + " в базе данных " + dbNameForMessageAndLogger + "</message>");
            }
            MotoListResponse motoListResponse = new MotoListResponse(motos);
            log.info("CarController getCarsByBrand, the motos the user was looking for were found in database " + dbNameForMessageAndLogger);
            return ResponseEntity.ok(motoListResponse);
        }
        List<Car> cars = carRepository.findAllByBrandIgnoreCase(brand);
        if (cars.isEmpty()) {
            log.warn("CarController getCarsByBrand, the cars the user was looking for were not found in database " + dbNameForMessageAndLogger);
            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + brand + " в базе данных " + dbNameForMessageAndLogger + "</message>");
        }
        CarListResponse carListResponse = new CarListResponse(cars);

        log.info("CarController getCarsByBrand, the cars the user was looking for were found");
        return ResponseEntity.ok(carListResponse);

    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> addVehicle(@RequestBody String requestBody, @RequestParam(value = "databaseName", required = false, defaultValue = "auto") Optional<String> databaseName) throws JsonProcessingException {
        Vehicle vehicle;
        ObjectMapper mapper = new ObjectMapper();

        Class<? extends Vehicle> vehicleType = (databaseName.get().equals("moto")) ? Moto.class : Car.class;

        vehicle = mapper.readValue(requestBody, vehicleType);

        if (vehicle instanceof Moto moto) {
            try {
                motoDAO.save(moto);
                log.info("VehicleController addVehicle, vehicle was added to " + databaseName.get());
                return ResponseEntity.status(HttpStatus.CREATED).body("<message>Запись была добавлена в базу данных moto</message>");
            } catch (Exception e) {
                log.warn("CarController addVehicle, vehicle was not added to " + databaseName.get());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Запись не была добавлена в базу данных " + databaseName.get() + "</message>");
            }
        } else {
            try {
                Car car = (Car) vehicle;
                carDAO.save(car);
                log.info("VehicleController addVehicle, vehicle was added to auto");
                return ResponseEntity.status(HttpStatus.CREATED).body("<message>Запись была добавлена в базу данных auto</message>");
            } catch (Exception e) {
                log.warn("CarController addCar, vehicle was not added to auto");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Запись не была добавлена в базу данных auto</message>");
            }
        }
    }

}
