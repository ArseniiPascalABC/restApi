package com.SSS.restApi.controllers.rest;


import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.Vehicle;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.xmlWrapper.MotoListResponse;
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
@RequestMapping("/rest/motos")
public class MotoVehicleController implements VehicleController {

    private final MotoRepository motoRepository;
    private final MotoDAO motoDao;

    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        Vehicle vehicle = motoRepository.findById(id).orElse(null);
        if (vehicle == null) {
            log.warn("MotoController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
            return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + id + " в базе данных motos</message>");
        }
        log.info("MotoController getVehicleById, the vehicle the user was looking for was found in database motos");
        return ResponseEntity.ok(vehicle);
    }

    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(brand);
        if (motos.isEmpty()) {
            log.warn("MotoController getVehiclesByBrand, the s the user was looking for were not found in database motos");
            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + brand + " в базе данных motos</message>");
        }
        MotoListResponse motoListResponse = new MotoListResponse(motos);
        log.info("MotoController getVehiclesByBrand, the s the user was looking for were found");
        return ResponseEntity.ok(motoListResponse);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> addVehicle(@RequestBody String requestBody) {
        try {
            Vehicle vehicle;
            ObjectMapper mapper = new ObjectMapper();
            vehicle = mapper.readValue(requestBody, Moto.class);
            motoDao.save(vehicle);
            log.info("MotoController addVehicle,  was added");
            return ResponseEntity.status(HttpStatus.CREATED).body("<message>Запись добавлена в motos</message>");
        } catch (Exception e) {
            log.info("MotoController addVehicle,  was not added");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Запись не была добавлена в motos</message>");
        }
    }


}
