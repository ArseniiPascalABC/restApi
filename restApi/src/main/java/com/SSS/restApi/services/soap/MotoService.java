package com.sss.restapi.services.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.restapi.dao.MotoDAO;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.repositories.moto.MotoRepository;
import com.sss.restapi.xmlwrapper.soap.SoapMotoListResponse;
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

    private static final String MESSAGE = "Message";
    private static final String SUCCESS = "Success";
    private final MotoDAO motoDAO;
    private final MotoRepository motoRepository;

    public JSONObject processMessageAndGetResponse(String message) throws JsonProcessingException {
        String method = "";
        String body = "";
        JSONObject jsonReplyMessage = new JSONObject();
        try {
            JSONObject jsonMessage = new JSONObject(message);
            method = jsonMessage.getString("Method");
            body = jsonMessage.getString("Body");
        } catch (JSONException e) {
            log.warn("Исключение {}", e.getMessage());
            jsonReplyMessage.put(MESSAGE, "Ошибка в переданных значениях");
            jsonReplyMessage.put(SUCCESS, false);
        }
        switch (method) {
            case "getVehicleById" -> {
                Moto moto = motoRepository.findById(Long.parseLong(body)).orElse(null);
                if (moto == null) {
                    log.warn(
                            "MotoSoapController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
                    jsonReplyMessage.put(MESSAGE, "Запись не найдена");
                } else {
                    log.info(
                            "MotoSoapController getVehicleById, the vehicle the user was looking for was found in database motos");
                    jsonReplyMessage.put(MESSAGE, "Запись найдена");
                    ObjectMapper mapper = new ObjectMapper();
                    jsonReplyMessage.put("Moto", mapper.writeValueAsString(moto));
                }
                jsonReplyMessage.put(SUCCESS, true);
            }
            case "getVehiclesByBrand" -> {
                List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(body);
                if (motos.isEmpty()) {
                    log.warn(
                            "MotoController getVehiclesByBrand, the vehicles the user was looking for were not found in database motos");
                    jsonReplyMessage.put(MESSAGE, "Авто не найдены");
                } else {
                    SoapMotoListResponse motoListResponse = new SoapMotoListResponse(motos);
                    jsonReplyMessage.put(MESSAGE, "Авто найдены");
                    ObjectMapper mapper = new ObjectMapper();
                    jsonReplyMessage.put("Data", mapper.writeValueAsString(motoListResponse));
                    log.info("MotoController getVehiclesByBrand, the vehicles the user was looking for were found");
                }
                jsonReplyMessage.put(SUCCESS, true);
            }
            case "addVehicle" -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Moto moto = mapper.readValue(body, Moto.class);
                    motoDAO.save(moto);
                    log.info("MotoSoapController addVehicle, Moto was added");
                    jsonReplyMessage.put(MESSAGE, "Запись добавлена");
                    jsonReplyMessage.put(SUCCESS, true);
                } catch (Exception e) {
                    log.error("MotoController addVehicle, Moto was not added {}", e.getMessage());
                    jsonReplyMessage.put(MESSAGE, "Запись не добавлена");
                    jsonReplyMessage.put(SUCCESS, false);
                }
            }
            default -> {
                jsonReplyMessage.put(MESSAGE, "Ошибка в переданном Method");
                jsonReplyMessage.put(SUCCESS, false);
            }
        }
        return jsonReplyMessage;
    }
}
