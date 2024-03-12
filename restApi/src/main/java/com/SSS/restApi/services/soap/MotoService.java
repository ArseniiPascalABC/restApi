package com.SSS.restApi.services.soap;

import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.xmlWrapper.soap.SoapMotoListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MotoService {

    private final MotoDAO motoDAO;
    private final MotoRepository motoRepository;

    public JSONObject processMessageAndGetResponse(String message) {
        JSONObject jsonReplyMessage = new JSONObject();
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String method = jsonMessage.getString("Method");
            String body = jsonMessage.getString("Body");
            switch (method) {
                case "getVehicleById" -> {
                    Moto moto = motoRepository.findById(Long.parseLong(body)).orElse(null);
                    if (moto == null) {
                        log.warn("MotoSoapController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
                        jsonReplyMessage.put("Message", "Запись не найдена");
                    } else {
                        log.info("MotoSoapController getVehicleById, the vehicle the user was looking for was found in database motos");
                        jsonReplyMessage.put("Message", "Запись найдена");
                        ObjectMapper mapper = new ObjectMapper();
                        jsonReplyMessage.put("Moto", mapper.writeValueAsString(moto));
                    }
                    jsonReplyMessage.put("Success", true);
                }
                case "getVehiclesByBrand" -> {
                    List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(body);
                    if (motos.isEmpty()) {
                        log.warn("MotoController getVehiclesByBrand, the vehicles the user was looking for were not found in database motos");
                        jsonReplyMessage.put("Message", "Авто не найдены");
                    } else {
                        SoapMotoListResponse motoListResponse = new SoapMotoListResponse(motos);
                        jsonReplyMessage.put("Message", "Авто найдены");
                        ObjectMapper mapper = new ObjectMapper();
                        jsonReplyMessage.put("Data", mapper.writeValueAsString(motoListResponse));
                        log.info("MotoController getVehiclesByBrand, the vehicles the user was looking for were found");
                    }
                    jsonReplyMessage.put("Success", true);
                }
                case "addVehicle" -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Moto moto = mapper.readValue(body, Moto.class);
                        motoDAO.save(moto);
                        log.info("MotoSoapController addVehicle, Moto was added");
                        jsonReplyMessage.put("Message", "Запись добавлена");
                        jsonReplyMessage.put("Success", true);
                    } catch (Exception e) {
                        log.error("MotoController addVehicle, Moto was not added " + e);
                        jsonReplyMessage.put("Message", "Запись не добавлена");
                        jsonReplyMessage.put("Success", false);
                    }
                }
                default -> {
                    jsonReplyMessage.put("Message", "Ошибка в переданном Method");
                    jsonReplyMessage.put("Success", false);
                }
            }
        } catch (JSONException e) {
            log.warn("Исключение " + e);
            jsonReplyMessage.put("Message", "Ошибка в переданных значениях");
            jsonReplyMessage.put("Success", false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonReplyMessage;
    }
}
