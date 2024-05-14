package com.sss.restapi.controllers.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.responses.soap.MotoResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MotoServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MotoServiceImplTest {
    @Autowired
    private MotoServiceImpl motoServiceImpl;

    @MockBean
    private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    /**
     * Method under test: {@link MotoServiceImpl#getVehicleById(Long)}
     */
    @Test
    void testGetVehicleById() throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        // Act
        MotoResponse actualVehicleById = motoServiceImpl.getVehicleById(1L);

        // Assert
        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualVehicleById.getMessage());
        assertNull(actualVehicleById.getData());
        assertFalse(actualVehicleById.isSuccess());
    }

    /**
     * Method under test: {@link MotoServiceImpl#getVehiclesByBrand(String)}
     */
    @Test
    void testGetVehiclesByBrand() throws JsonProcessingException {
        // Arrange
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        // Act
        MotoResponse actualVehiclesByBrand = motoServiceImpl.getVehiclesByBrand("Brand");

        // Assert
        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualVehiclesByBrand.getMessage());
        assertNull(actualVehiclesByBrand.getData());
        assertFalse(actualVehiclesByBrand.isSuccess());
    }

    /**
     * Method under test: {@link MotoServiceImpl#addVehicle(Moto)}
     */
    @Test
    void testAddVehicle() throws JsonProcessingException {
        // Arrange
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        Moto moto = new Moto();
        moto.setBrand("Brand");
        moto.setId(1L);
        moto.setModel("Model");

        // Act
        MotoResponse actualAddVehicleResult = motoServiceImpl.addVehicle(moto);

        // Assert
        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualAddVehicleResult.getMessage());
        assertNull(actualAddVehicleResult.getData());
        assertFalse(actualAddVehicleResult.isSuccess());
    }

    /**
     * Method under test:
     * {@link MotoServiceImpl#getJsonObject(JSONObject, ReplyingKafkaTemplate, String)}
     */
    @Test
    void testGetJsonObject() {
        // Arrange
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);
        JSONObject jsonMessage = new JSONObject();

        // Act
        JSONObject actualJsonObject = MotoServiceImpl.getJsonObject(jsonMessage, replyingKafkaTemplate, "Topic Name");

        // Assert
        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertNull(actualJsonObject);
    }
}
