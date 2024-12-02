package com.sss.restapi.services.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class ServiceForKafka {
    private final CarService carService;
    private final MotoService motoService;

    @KafkaListener(topics = "${kafka.soap.topic.name}", groupId = "restSoap-group", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public String processMessageAndGetResponse(@Payload String message) {
        JSONObject jsonReplyMessage = new JSONObject();
        try {
            JSONObject jsonMessage = new JSONObject(message);
            String vehicle = jsonMessage.getString("Vehicle");
            switch (vehicle) {
                case "Moto" -> jsonReplyMessage = motoService.processMessageAndGetResponse(message);
                case "Car" -> jsonReplyMessage = carService.processMessageAndGetResponse(message);
                default -> {
                    log.error("default Service For Kafka, Ошибка в переданном транспорте");
                    jsonReplyMessage.put("Message", "Ошибка в переданном транспорте");
                    jsonReplyMessage.put("Success", false);
                }
            }
        } catch (JSONException | JsonProcessingException e) {
            log.warn("Исключение {}", e.getMessage());
            jsonReplyMessage.put("Message", "Ошибка в переданных значениях");
            jsonReplyMessage.put("Success", false);
        }
        return jsonReplyMessage.toString();
    }
}
