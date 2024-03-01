package com.SSS.restApi.services.soap;

import com.SSS.restApi.dao.CarDAO;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.repositories.car.CarRepository;
import com.SSS.restApi.responses.soap.CarResponse;
import com.SSS.restApi.xmlWrapper.soap.SoapCarListResponse;
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

public class CarServiceForKafka {

    private final CarDAO carDAO;
    private final CarRepository carRepository;
    private final AtomicBoolean isMessageProcessed = new AtomicBoolean(false);
    @KafkaListener(topics = "soapTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
    public CarResponse processMessageAndGetResponse(@Payload String message) {
        CarResponse response = new CarResponse();
        if (isMessageProcessed.get()) {
            isMessageProcessed.set(false);
            return response;
        }
        isMessageProcessed.set(true);
        try {
            System.out.println("Мы в try");
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
                            response.setMessage("Запись не найдена");
                        } else {
                            log.info("CarSoapController getVehicleById, the vehicle the user was looking for was found in database cars");
                            response.setMessage("Запись найдена");
                            response.setData(car);
                        }
                        response.setSuccess(true);
                    } else {
                        response.setMessage("Некорректно переданный Vehicle");
                        response.setSuccess(false);
                    }
                }
                case "getVehiclesByBrand" -> {
                    if ("Car".equals(vehicle)) {
                        List<Car> cars = carRepository.findAllByBrandIgnoreCase(body);
                        if (cars.isEmpty()) {
                            log.warn("CarController getVehiclesByBrand, the vehicles the user was looking for were not found in database cars");
                            response.setMessage("Авто не найдены");
                        } else {
                            SoapCarListResponse carListResponse = new SoapCarListResponse(cars);
                            response.setMessage("Авто найдены");
                            response.setData(carListResponse);
                            log.info("CarController getVehiclesByBrand, the vehicles the user was looking for were found");
                        }
                        response.setSuccess(true);
                    } else {
                        response.setMessage("Некорректно переданный Vehicle");
                        response.setSuccess(false);
                    }
                    return response;
                }
                case "addVehicle" -> {
                    System.out.println("Мы в addVehicle");
                    if ("Car".equals(vehicle)) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            System.out.println("1");
                            Car car = mapper.readValue(body, Car.class);
                            System.out.println("1");
                            carDAO.save(car);
                            System.out.println("1");
                            log.info("CarSoapController addVehicle, Car was added");
                            response.setMessage("Запись добавлена");
                            response.setSuccess(true);
                            return response;
                        } catch (Exception e) {
                            log.error("CarController addVehicle, Car was not added " + e);
                            response.setMessage("Запись не добавлена");
                            response.setSuccess(false);
                            return response;
                        }
                    } else {
                        response.setMessage("Ошибка в переданном Vehicle");
                        response.setSuccess(false);
                        return response;
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
