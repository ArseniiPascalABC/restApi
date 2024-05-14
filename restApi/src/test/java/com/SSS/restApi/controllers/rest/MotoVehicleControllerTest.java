package com.sss.restapi.controllers.rest;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.restapi.services.rest.MotoVehicleService;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {MotoVehicleController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MotoVehicleControllerTest {
    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MotoVehicleController motoVehicleController;

    @MockBean
    private MotoVehicleService motoVehicleService;


    @Test
    void testAddVehicle() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(motoVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/rest/motos/")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString("foo"));

        MockMvcBuilders.standaloneSetup(motoVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehicleById() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(motoVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/rest/motos/");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("id", String.valueOf(1L));

        MockMvcBuilders.standaloneSetup(motoVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehiclesByBrand() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(motoVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/motos/brand")
                .param("brand", "foo");

        MockMvcBuilders.standaloneSetup(motoVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetVehiclesByBrand2() throws Exception {
        when(kafkaTemplate.send(Mockito.any(), Mockito.any())).thenReturn(new CompletableFuture<>());
        when(motoVehicleService.processMessageAndGetResponse(Mockito.any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/motos/brand")
                .param("brand", "https://example.org/example");

        MockMvcBuilders.standaloneSetup(motoVehicleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
