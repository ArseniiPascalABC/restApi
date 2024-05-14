package com.sss.restapi.controllers.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sss.restapi.models.car.Car;
import com.sss.restapi.responses.soap.CarResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
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

@ContextConfiguration(classes = {CarServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CarServiceImplTest {
    @Autowired
    private CarServiceImpl carServiceImpl;

    @MockBean
    private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Test
    void testGetVehicleById() throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        CarResponse actualVehicleById = carServiceImpl.getVehicleById(1L);

        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualVehicleById.getMessage());
        assertNull(actualVehicleById.getData());
        assertFalse(actualVehicleById.isSuccess());
    }

    @Test
    void testGetVehiclesByBrand()
            throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        CarResponse actualVehiclesByBrand = carServiceImpl.getVehiclesByBrand("Brand");

        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualVehiclesByBrand.getMessage());
        assertNull(actualVehiclesByBrand.getData());
        assertFalse(actualVehiclesByBrand.isSuccess());
    }

    @Test
    void testAddVehicle() throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
        RequestReplyFuture<String, String, String> requestReplyFuture = mock(RequestReplyFuture.class);
        when(requestReplyFuture.getSendFuture()).thenReturn(new CompletableFuture<>());
        when(replyingKafkaTemplate.sendAndReceive(Mockito.<ProducerRecord<String, String>>any()))
                .thenReturn(requestReplyFuture);

        Car car = new Car();
        car.setBrand("Brand");
        car.setId(1L);
        car.setModel("Model");

        CarResponse actualAddVehicleResult = carServiceImpl.addVehicle(car);

        verify(replyingKafkaTemplate).sendAndReceive(isA(ProducerRecord.class));
        verify(requestReplyFuture).getSendFuture();
        assertEquals("Error sending or receiving message from Kafka", actualAddVehicleResult.getMessage());
        assertNull(actualAddVehicleResult.getData());
        assertFalse(actualAddVehicleResult.isSuccess());
    }
}
