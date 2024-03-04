package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.responses.soap.MotoResponse;
import com.SSS.restApi.xmlWrapper.soap.SoapMotoListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@WebService(serviceName = "MotoService", endpointInterface = "com.SSS.restApi.controllers.soap.MotoService")
public class MotoServiceImpl implements MotoService{

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Override
    public MotoResponse getVehicleById(Long id) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
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
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        System.out.println(jsonMessage);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
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
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "addVehicle");
        jsonMessage.put("Body", motoJson);
        System.out.println(jsonMessage);
        JSONObject replyJson = sendAndReceiveMessage(jsonMessage);
        if (replyJson == null) {
            return new MotoResponse(null, "Error sending or receiving message from Kafka", false);
        }
        boolean success = replyJson.getBoolean("Success");
        String message = replyJson.getString("Message");
        return new MotoResponse(null, message, success);
    }

    private JSONObject sendAndReceiveMessage(JSONObject jsonMessage) {
        return getJsonObject(jsonMessage, replyingKafkaTemplate, "soapMotoTopic");
    }

    @Nullable
    static JSONObject getJsonObject(JSONObject jsonMessage, ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate, String topicName) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, jsonMessage.toString());
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
        SendResult<String, String> sendResult;
        try {
            sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            MotoServiceImpl.log.info("Sent ok: " + sendResult.getRecordMetadata());
            ConsumerRecord<String, String> consumerRecord = replyFuture.get();
            MotoServiceImpl.log.info("Return value: " + consumerRecord.value());
            return new JSONObject(consumerRecord.value());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            MotoServiceImpl.log.error("Error sending or receiving message from Kafka: " + e.getMessage());
            return null;
        }
    }
    //rest запрос к soap сервису
}
