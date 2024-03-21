package com.sss.restapi.controllers.soap;

import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.responses.soap.MotoResponse;
import com.sss.restapi.xmlwrapper.soap.SoapMotoListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "MotoService", endpointInterface = "com.sss.restapi.controllers.soap.MotoService")
@Component
@ComponentScan("application.properties")
public class MotoServiceImpl implements MotoService{
    private static final String VEHICLE = "Vehicle";
    private static final String METHOD = "Method";
    private static final String BODY = "Body";
    private static final String SUCCESS = "Success";
    private static final String MESSAGE = "Message";
    private static final String ERROR_MESSAGE = "Error sending or receiving message from Kafka";
    
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    
    @Value("${kafka.soap.topic.name}")
    public String soapTopicName;

    @Override
    public MotoResponse getVehicleById(Long id) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "getVehicleById");
        jsonMessage.put(BODY, id.toString());
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
        Moto moto;
        if (replyJson.has("Moto")) {
            ObjectMapper mapper = new ObjectMapper();
            moto = mapper.readValue(replyJson.getString("Moto"), Moto.class);
        } else {
            moto = null;
        }
        return new MotoResponse(moto, message, success);
    }

    @Override
    public MotoResponse getVehiclesByBrand(String brand) throws JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "getVehiclesByBrand");
        jsonMessage.put(BODY, brand);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
        SoapMotoListResponse soapMotoListResponse;
        if (replyJson.has("Data")) {
            ObjectMapper mapper = new ObjectMapper();
            soapMotoListResponse = mapper.readValue(replyJson.getString("Data"), SoapMotoListResponse.class);
        } else {
            soapMotoListResponse = null;
        }
        return new MotoResponse(soapMotoListResponse, message, success);
    }
    @Override
    public MotoResponse addVehicle(Moto moto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String motoJson = mapper.writeValueAsString(moto);
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(VEHICLE, "Moto");
        jsonMessage.put(METHOD, "addVehicle");
        jsonMessage.put(BODY, motoJson);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, ERROR_MESSAGE, false);
        }
        boolean success = replyJson.getBoolean(SUCCESS);
        String message = replyJson.getString(MESSAGE);
        return new MotoResponse(null, message, success);
    }

    private JSONObject sendAndReceiveMessage(JSONObject jsonMessage) {
        return getJsonObject(jsonMessage, replyingKafkaTemplate, soapTopicName);
    }

    @Nullable
    static JSONObject getJsonObject(JSONObject jsonMessage, ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate, String topicName) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, jsonMessage.toString());
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        SendResult<String, String> sendResult;
        try {
            sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            MotoServiceImpl.log.info("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, String> consumerRecord = replyFuture.get();
            MotoServiceImpl.log.info("Return value: " + consumerRecord.value());
            return new JSONObject(consumerRecord.value());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            MotoServiceImpl.log.error("Error sending or receiving message from Kafka: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
