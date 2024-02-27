//package com.SSS.restApi.services.soap;
//
//import com.SSS.restApi.models.car.Car;
//import com.SSS.restApi.xmlWrapper.rest.CarListResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.annotation.KafkaListener;
//
//import java.util.List;
//
//public class CarServiceForKafka {
////    @Override
//    @KafkaListener(topics = "soapTopic", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
//    public Car getVehicleById(String message) {
//        try {
//            JSONObject jsonMessage = new JSONObject(message);
//            String method = jsonMessage.getString("Method");
//            String vehicle = jsonMessage.getString("Vehicle");
//            String body = jsonMessage.getString("Body");
//            switch (method){
//                case "getVehicleById" -> {
//                    if ("Car".equals(vehicle)){
//                        Car car = carRepository.findById(Long.parseLong(body)).orElse(null);
//                        if (car == null) {
//                            log.warn("CarController getVehicleById, the vehicle the user was looking for(in dataBase cars) was not found");
//                            return ResponseEntity.ok().body("<message>Ничего не найдено по id = " + body + " в базе данных cars</message>");
//                        } else {
//                            log.info("CarController getVehicleById, the vehicle the user was looking for was found in database cars");
//                            return ResponseEntity.ok(car);
//                        }
//                    }
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
//                }
//                case "getVehiclesByBrand" -> {
//                    if ("Car".equals(vehicle)) {
//                        List<Car> cars = carRepository.findAllByBrandIgnoreCase(body);
//                        if (cars.isEmpty()) {
//                            log.warn("CarController getVehiclesByBrand, the vehicles the user was looking for were not found in database cars");
//                            return ResponseEntity.ok().body("<message>Ничего не найдено по бренду " + body + " в базе данных cars</message>");
//                        }
//                        CarListResponse carListResponse = new CarListResponse(cars);
//                        log.info("CarController getVehiclesByBrand, the vehicles the user was looking for were found");
//                        return ResponseEntity.ok(carListResponse);
//                    }
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
//                }
//                case "addVehicle" -> {
//                    if ("Car".equals(vehicle)) {
//                        try {
//                            ObjectMapper mapper = new ObjectMapper();
//                            Car car = mapper.readValue(body, Car.class);
//                            carDAO.save(car);
//                            log.info("CarController addVehicle, Car was added");
//                            return ResponseEntity.status(HttpStatus.CREATED).body("<message>Автомобиль добавлен</message>");
//                        } catch (Exception e) {
//                            log.error("CarController addVehicle, Car was not added");
//                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<message>Автомобиль не был добавлен</message>");
//                        }
//                    }
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported vehicle");
//                }
//                default -> {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported operation");
//                }
//            }
//        } catch (JSONException e) {
//            log.warn("Исключение " + e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message");
//        }
//    }
//}
