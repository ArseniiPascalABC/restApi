package com.sss.restapi.services.soap;

import com.sss.restapi.dao.CarDAO;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.repositories.car.CarRepository;
import com.sss.restapi.xmlwrapper.soap.SoapCarListResponse;
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
public class CarService {
    private static final String MESSAGE = "Message";
    private static final String SUCCESS = "Success";
    private final CarDAO carDAO;
    private final CarRepository carRepository;

    public JSONObject processMessageAndGetResponse(String message) throws JsonProcessingException {
        String method = "";
        String body = "";
        JSONObject jsonReplyMessage = new JSONObject();
        try {
            JSONObject jsonMessage = new JSONObject(message);
            method = jsonMessage.getString("Method");
            body = jsonMessage.getString("Body");
        } catch (JSONException e) {
            log.warn("Исключение " + e);
            jsonReplyMessage.put(MESSAGE, "Ошибка в переданных значениях");
            jsonReplyMessage.put(SUCCESS, false);
        }
        switch (method) {
            case "getVehicleById" -> {
                Car car = carRepository.findById(Long.parseLong(body)).orElse(null);
                if (car == null) {
                    log.warn("CarSoapController getVehicleById, the vehicle the user was looking for(in dataBase cars) was not found");
                    jsonReplyMessage.put(MESSAGE, "Запись не найдена");
                } else {
                    log.info("CarSoapController getVehicleById, the vehicle the user was looking for was found in database cars");
                    jsonReplyMessage.put(MESSAGE, "Запись найдена");
                    ObjectMapper mapper = new ObjectMapper();
                    jsonReplyMessage.put("Car", mapper.writeValueAsString(car));
                }
                jsonReplyMessage.put(SUCCESS, true);
            }
            case "getVehiclesByBrand" -> {
                List<Car> cars = carRepository.findAllByBrandIgnoreCase(body);
                if (cars.isEmpty()) {
                    log.warn("CarController getVehiclesByBrand, the vehicles the user was looking for were not found in database cars");
                    jsonReplyMessage.put(MESSAGE, "Авто не найдены");
                } else {
                    SoapCarListResponse carListResponse = new SoapCarListResponse(cars);
                    jsonReplyMessage.put(MESSAGE, "Авто найдены");
                    ObjectMapper mapper = new ObjectMapper();
                    jsonReplyMessage.put("Data", mapper.writeValueAsString(carListResponse));
                    log.info("CarController getVehiclesByBrand, the vehicles the user was looking for were found");
                }
                jsonReplyMessage.put(SUCCESS, true);
            }
            case "addVehicle" -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Car car = mapper.readValue(body, Car.class);
                    carDAO.save(car);
                    log.info("CarSoapController addVehicle, Car was added");
                    jsonReplyMessage.put(MESSAGE, "Запись добавлена");
                    jsonReplyMessage.put(SUCCESS, true);
                } catch (Exception e) {
                    log.error("CarController addVehicle, Car was not added " + e);
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
