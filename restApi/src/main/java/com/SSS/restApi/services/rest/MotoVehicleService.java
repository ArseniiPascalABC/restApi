package com.SSS.restApi.services.rest;

import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.xmlWrapper.rest.MotoListResponse;
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
    private final MotoRepository motoRepository;
    private final MotoDAO motoDAO;
    @Override
    @KafkaListener(topics = "restTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
    public ResponseEntity<Object> processMessageAndGetResponse(String message) {
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String method = jsonMessage.getString("Method");
            String vehicle = jsonMessage.getString("Vehicle");
            String body = jsonMessage.getString("Body");
            switch (method){
                case "getVehicleById" -> {
                    if ("Moto".equals(vehicle)){
                        Moto moto = motoRepository.findById(Long.parseLong(body)).orElse(null);
                        if (moto == null) {
                            log.warn("MotoController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
                            return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + body + " в базе данных motos</message>");
                        } else {
                            log.info("MotoController getVehicleById, the vehicle the user was looking for was found in database motos");
                            return ResponseEntity.ok(moto);
                        }
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
                }
                case "getVehiclesByBrand" -> {
                    if ("Moto".equals(vehicle)) {
                        List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(body);
                        if (motos.isEmpty()) {
                            log.warn("MotoController getVehiclesByBrand, the vehicles the user was looking for were not found in database motos");
                            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + body + " в базе данных motos</message>");
                        }
                        MotoListResponse motoListResponse = new MotoListResponse(motos);
                        log.info("MotoController getVehiclesByBrand, the vehicles the user was looking for were found");
                        return ResponseEntity.ok(motoListResponse);
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
                }
                case "addVehicle" -> {
                    if ("Moto".equals(vehicle)) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            Moto moto = mapper.readValue(body, Moto.class);
                            motoDAO.save(moto);
                            log.info("MotoController addVehicle, Moto was added");
                            return ResponseEntity.status(HttpStatus.CREATED).body("<message>Мотоцикл добавлен</message>");
                        } catch (Exception e) {
                            log.error("MotoController addVehicle, Moto was not added");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Мотоцикл не был добавлен</message>");
                        }
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported operation");
                }
            }
        } catch (JSONException e) {
            log.warn("Исключение " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message");
        }
    }
}
