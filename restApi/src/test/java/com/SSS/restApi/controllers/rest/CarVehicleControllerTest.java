package com.sss.restapi.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.restapi.controllers.soap.CarServiceImpl;
import com.sss.restapi.services.rest.CarVehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CarVehicleController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CarVehicleControllerTest {
    @Autowired
    private CarVehicleController carVehicleController;

    @MockBean
    private CarVehicleService carVehicleService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testAddVehicle() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(carVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/rest/cars/")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString("foo"));

        MockMvcBuilders.standaloneSetup(carVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehicleById() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(carVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/rest/cars/");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("id", String.valueOf(1L));

        MockMvcBuilders.standaloneSetup(carVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehiclesByBrand() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(carVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/cars/brand").param("brand", "foo");

        MockMvcBuilders.standaloneSetup(carVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehiclesByBrand2() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.<String>any())).thenReturn(new CompletableFuture<>());
        when(carVehicleService.processMessageAndGetResponse(Mockito.<String>any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/cars/brand")
                .param("brand", "https://example.org/example");

        MockMvcBuilders.standaloneSetup(carVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testSoapRequestGetVehicleById() throws Exception {
        ProducerFactory<String, String> producerFactory = mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        DefaultKafkaConsumerFactory<? super String, ? super String> consumerFactory = new DefaultKafkaConsumerFactory<>(
                new HashMap<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/rest/cars/soapRequest",
                        new CarServiceImpl(new ReplyingKafkaTemplate<>(producerFactory,
                                new ConcurrentMessageListenerContainer<>(consumerFactory,
                                        new ContainerProperties(Pattern.compile(".*\\.txt"))),
                                true)))
                .param("id", "https://example.org/example");

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(carVehicleController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
