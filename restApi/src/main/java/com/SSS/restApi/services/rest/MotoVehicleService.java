package com.sss.restapi.services.rest;

import com.sss.restapi.dao.MotoDAO;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.repositories.moto.MotoRepository;
import com.sss.restapi.xmlwrapper.rest.MotoListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MotoVehicleService implements VehicleService{
    private static final String UNSUPPORTED_VEHICLE = "Unsupported vehicle";

    private final MotoRepository motoRepository;

    private final MotoDAO motoDAO;
    @Override
    @KafkaListener(topics = "restTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
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
            log.warn("Исключение " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message");
        }
        if ("Moto".equals(vehicle)) {
            switch (method) {
                case "getVehicleById" -> {
                    Moto moto = motoRepository.findById(Long.parseLong(body)).orElse(null);
                    if (moto == null) {
                        log.warn("MotoController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
                        return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + body + " в базе данных motos</message>");
                    } else {
                        log.info("MotoController getVehicleById, the vehicle the user was looking for was found in database motos");
                        return ResponseEntity.ok(moto);
                    }
                }
                case "getVehiclesByBrand" -> {
                    List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(body);
                    if (motos.isEmpty()) {
                        log.warn("MotoController getVehiclesByBrand, the vehicles the user was looking for were not found in database motos");
                        return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + body + " в базе данных motos</message>");
                    }
                    MotoListResponse motoListResponse = new MotoListResponse(motos);
                    log.info("MotoController getVehiclesByBrand, the vehicles the user was looking for were found");
                    return ResponseEntity.ok(motoListResponse);
                }
                case "addVehicle" -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Moto moto = mapper.readValue(body, Moto.class);
                        motoDAO.save(moto);
                        log.info("MotoController addVehicle, Moto was added");
                        return ResponseEntity.status(HttpStatus.CREATED).body("<message>Автомобиль добавлен</message>");
                    } catch (Exception e) {
                        log.error("MotoController addVehicle, Moto was not added");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Автомобиль не был добавлен</message>");
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
