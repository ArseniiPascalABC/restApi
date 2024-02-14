package com.SSS.restApi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SSS.restApi.models.Car;
import com.SSS.restApi.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Car car1 = new Car(1L, "Toyota", "Camry");
        Car car2 = new Car(2L, "Toyota", "Corolla");
        Car car3 = new Car(3L, "Honda", "Civic");
        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 OK и нужным авто")
    public void testGetCarById() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Car><id>1</id><brand>Toyota</brand><model>Camry</model></Car>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 ОК при отсутствии машины")
    public void testGetNonExistentCarById() throws Exception {
        String expectedMessage = "<message>Ничего не найдено</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "999")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK и нужными автомобилями")
    public void testGetCarsByBrand() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Cars><Car><id>1</id><brand>Toyota</brand><model>Camry</model></Car>" +
                "<Car><id>2</id><brand>Toyota</brand><model>Corolla</model></Car></Cars>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "Toyota")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK при отсутствии машин нужного бренда")
    public void testGetNonExistentCarsByBrand() throws Exception {

        String expectedXml = "<message>Ничего не найдено по бренду qqq</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "qqq")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("POST / добавляет автомобиль и возвращает сообщение о добавлении")
    public void testAddCar() throws Exception {
        Car car = new Car(4L, "Toyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись добавлена</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / не добавляет автомобиль и возвращает сообщение об ошибке")
    public void testBadRequestAddCar() throws Exception {
        Car car = new Car(4L, "ToyotaToyotaToyotaToyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись не была добавлена</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

}
