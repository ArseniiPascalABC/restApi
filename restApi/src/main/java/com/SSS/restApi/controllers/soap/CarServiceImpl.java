package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.responses.soap.CarResponse;
import com.SSS.restApi.services.soap.CarServiceForKafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
@WebService(
        serviceName = "CarService",
        endpointInterface = "com.SSS.restApi.controllers.soap.CarService")
public class CarServiceImpl implements CarService{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CarServiceForKafka carServiceForKafka;

    @Override
    public CarResponse getVehicleById(Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        System.out.println(jsonMessage);
        kafkaTemplate.send("soapTopic", jsonMessage.toString());
        return carServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    public CarResponse getVehiclesByBrand(String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        System.out.println(jsonMessage);
        kafkaTemplate.send("soapTopic", jsonMessage.toString());
        return carServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
    }
    @Override
    public CarResponse addVehicle(Car car) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String carJson = mapper.writeValueAsString(car);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "addVehicle");
        jsonMessage.put("Body", carJson);
        System.out.println(jsonMessage);
        kafkaTemplate.send("soapTopic", jsonMessage.toString());
        return carServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
    }
    //Отправка - ожидание ответа
    //rest запрос к soap сервису
}
