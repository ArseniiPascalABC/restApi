package com.SSS.restApi.services.soap;

import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.responses.soap.MotoResponse;
import com.SSS.restApi.xmlWrapper.soap.SoapMotoListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Slf4j
@Component
public class MotoServiceForKafka {

    private final MotoDAO motoDAO;
    private final MotoRepository motoRepository;
    private final AtomicBoolean isMessageProcessed = new AtomicBoolean(false);
    @KafkaListener(topics = "soapTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
    public MotoResponse processMessageAndGetResponse(@Payload String message) {
        MotoResponse response = new MotoResponse();
        if (isMessageProcessed.get()) {
            isMessageProcessed.set(false);
            return response;
        }
        isMessageProcessed.set(true);
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
                            log.warn("MotoSoapController getVehicleById, the vehicle the user was looking for(in dataBase motos) was not found");
                            response.setMessage("Запись не найдена");
                        } else {
                            log.info("MotoSoapController getVehicleById, the vehicle the user was looking for was found in database motos");
                            response.setMessage("Запись найдена");
                            response.setData(moto);
                        }
                        response.setSuccess(true);
                    } else {
                        response.setMessage("Некорректно переданный Vehicle");
                        response.setSuccess(false);
                    }
                }
                case "getVehiclesByBrand" -> {
                    if ("Moto".equals(vehicle)) {
                        List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(body);
                        if (motos.isEmpty()) {
                            log.warn("MotoController getVehiclesByBrand, the vehicles the user was looking for were not found in database motos");
                            response.setMessage("Мотоциклы не найдены");
                        } else {
                            SoapMotoListResponse soapMotoListResponse = new SoapMotoListResponse(motos);
                            response.setMessage("Мотоциклы найдены");
                            response.setData(soapMotoListResponse);
                            log.info("MotoController getVehiclesByBrand, the vehicles the user was looking for were found");
                        }
                        response.setSuccess(true);
                    } else {
                        response.setMessage("Некорректно переданный Vehicle");
                        response.setSuccess(false);
                    }
                }
                case "addVehicle" -> {
                    if ("Moto".equals(vehicle)) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            System.out.println("1");
                            Moto moto = mapper.readValue(body, Moto.class);
                            System.out.println("1");
                            motoDAO.save(moto);
                            System.out.println("1");
                            log.info("MotoSoapController addVehicle, Moto was added");
                            response.setMessage("Запись добавлена");
                            response.setSuccess(true);

                        } catch (Exception e) {
                            log.error("MotoController addVehicle, Moto was not added " + e);
                            response.setMessage("Запись не добавлена");
                            response.setSuccess(false);
                        }
                    } else {
                        response.setMessage("Ошибка в переданном Vehicle");
                        response.setSuccess(false);
                    }
                }
                default -> {
                    response.setMessage("Ошибка в переданном Method");
                    response.setSuccess(false);
                }
            }
        } catch (JSONException e) {
            log.warn("Исключение " + e);
            response.setMessage("Ошибка в переданных значениях");
            response.setSuccess(false);
        }
        isMessageProcessed.set(false);
        return response;
    }
}
