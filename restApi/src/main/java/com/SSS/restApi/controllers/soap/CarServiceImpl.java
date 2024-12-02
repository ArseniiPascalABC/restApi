package com.sss.restapi.controllers.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.responses.soap.CarResponse;
import com.sss.restapi.xmlwrapper.soap.SoapCarListResponse;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.sss.restapi.controllers.soap.MotoServiceImpl.getJsonObject;

@Component
@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "CarService", endpointInterface = "com.sss.restapi.controllers.soap.CarService")
public class CarServiceImpl implements CarService {
    private static final String VEHICLE = "Vehicle";
    private static final String METHOD = "Method";
    private static final String BODY = "Body";
    private static final String SUCCESS = "Success";
    private static final String MESSAGE = "Message";
    private static final String ERROR_MESSAGE = "Error sending or receiving message from Kafka";

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Override
    public CarResponse getVehicleById(Long id)
            throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "getVehicleById");
        jsonMessage.put(BODY, id.toString());
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
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
    public CarResponse getVehiclesByBrand(String brand)
            throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "getVehiclesByBrand");
        jsonMessage.put(BODY, brand);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
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
    public CarResponse addVehicle(Car car)
            throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        String carJson = mapper.writeValueAsString(car);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Car");
        jsonMessage.put(METHOD, "addVehicle");
        jsonMessage.put(BODY, carJson);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new CarResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
        return new CarResponse(null, message, success);
    }

    private JSONObject sendAndReceiveMessage(JSONObject jsonMessage) {
        return getJsonObject(jsonMessage, replyingKafkaTemplate, "soapTopic");
    }
}
