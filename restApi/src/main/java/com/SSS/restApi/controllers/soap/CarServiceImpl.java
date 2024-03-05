package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.responses.soap.CarResponse;
import com.SSS.restApi.xmlWrapper.soap.SoapCarListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.SSS.restApi.controllers.soap.MotoServiceImpl.getJsonObject;

@Component
@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "CarService", endpointInterface = "com.SSS.restApi.controllers.soap.CarService")
public class CarServiceImpl implements CarService{

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    @Override
    public CarResponse getVehicleById(Long id) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
        Car car;
        if (replyJson.has("Car")) {
            ObjectMapper mapper = new ObjectMapper();
            car = mapper.readValue(replyJson.getString("Car"), Car.class);
        } else {
            car = null;
        }
        return new CarResponse(car, message, success);
    }

    @Override
    public CarResponse getVehiclesByBrand(String brand) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
        SoapCarListResponse soapCarListResponse;
        if (replyJson.has("Data")) {
            ObjectMapper mapper = new ObjectMapper();
            soapCarListResponse = mapper.readValue(replyJson.getString("Data"), SoapCarListResponse.class);
        } else {
            soapCarListResponse = null;
        }
        return new CarResponse(soapCarListResponse, message, success);
    }
    @Override
    public CarResponse addVehicle(Car car) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        String carJson = mapper.writeValueAsString(car);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "addVehicle");
        jsonMessage.put("Body", carJson);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
        return new CarResponse(null, message, success);
    }

    private JSONObject sendAndReceiveMessage(JSONObject jsonMessage) {
        return getJsonObject(jsonMessage, replyingKafkaTemplate, "soapCarTopic");
    }
    //rest запрос к soap сервису
}
