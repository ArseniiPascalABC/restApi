package com.SSS.restApi.services.soap;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.xmlWrapper.soap.SoapCarListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class CarServiceForKafka {

    private final CarDAO carDAO;
    private final CarRepository carRepository;
    @KafkaListener(topics = "soapCarTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public String processMessageAndGetResponse(@Payload String message) {
        JSONObject jsonReplyMessage = new JSONObject();
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String method = jsonMessage.getString("Method");
            String vehicle = jsonMessage.getString("Vehicle");
            String body = jsonMessage.getString("Body");
            switch (method){
                case "getVehicleById" -> {
                    if ("Car".equals(vehicle)){
                        Car car = carRepository.findById(Long.parseLong(body)).orElse(null);
                        if (car == null) {
                            log.warn("CarSoapController getVehicleById, the vehicle the user was looking for(in dataBase cars) was not found");
                            jsonReplyMessage.put("Message", "Запись не найдена");
                        } else {
                            log.info("CarSoapController getVehicleById, the vehicle the user was looking for was found in database cars");
                            jsonReplyMessage.put("Message", "Запись найдена");
                            ObjectMapper mapper = new ObjectMapper();
                            jsonReplyMessage.put("Car", mapper.writeValueAsString(car));
                        }
                        jsonReplyMessage.put("Success", true);
                    } else {
                        jsonReplyMessage.put("Message", "Некорректно переданный Vehicle");
                        jsonReplyMessage.put("Success", false);
                    }
                }
                case "getVehiclesByBrand" -> {
                    if ("Car".equals(vehicle)) {
                        List<Car> cars = carRepository.findAllByBrandIgnoreCase(body);
                        if (cars.isEmpty()) {
                            log.warn("CarController getVehiclesByBrand, the vehicles the user was looking for were not found in database cars");
                            jsonReplyMessage.put("Message", "Авто не найдены");
                        } else {
                            SoapCarListResponse carListResponse = new SoapCarListResponse(cars);
                            jsonReplyMessage.put("Message", "Авто найдены");
                            ObjectMapper mapper = new ObjectMapper();
                            jsonReplyMessage.put("Data", mapper.writeValueAsString(carListResponse));
                            log.info("CarController getVehiclesByBrand, the vehicles the user was looking for were found");
                        }
                        jsonReplyMessage.put("Success", true);
                    } else {
                        jsonReplyMessage.put("Message", "Некорректно переданный Vehicle");
                        jsonReplyMessage.put("Success", false);
                    }
                }
                case "addVehicle" -> {
                    if ("Car".equals(vehicle)) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            Car car = mapper.readValue(body, Car.class);
                            carDAO.save(car);
                            log.info("CarSoapController addVehicle, Car was added");
                            jsonReplyMessage.put("Message", "Запись добавлена");
                            jsonReplyMessage.put("Success", true);
                        } catch (Exception e) {
                            log.error("CarController addVehicle, Car was not added " + e);
                            jsonReplyMessage.put("Message", "Запись не добавлена");
                            jsonReplyMessage.put("Success", false);
                        }
                    } else {
                        jsonReplyMessage.put("Message", "Ошибка в переданном Vehicle");
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
        return jsonReplyMessage.toString();
    }
}
