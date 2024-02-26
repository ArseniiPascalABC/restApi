package com.SSS.restApi.controllers;

import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.repositories.car.CarRepository;
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
public class VehicleCommonControllerTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Car car = new Car(1L, "Toyota", "Camry");
        Car car1 = new Car(2L, "Toyota", "Corolla");
        Car car2 = new Car(3L, "Honda", "Civic");
        Moto moto = new Moto(1L, "Toyota", "Camry");
        Moto moto1 = new Moto(2L, "Toyota", "Corolla");
        Moto moto2 = new Moto(3L, "Honda", "Civic");
        carRepository.save(car);
        carRepository.save(car1);
        carRepository.save(car2);
        motoRepository.save(moto);
        motoRepository.save(moto1);
        motoRepository.save(moto2);

    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 OK и нужным авто")
    public void testGetCarById() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Car><id>1</id><brand>Toyota</brand><model>Camry</model></Car>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "1")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 OK и нужным мото")
    public void testGetMotoById() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Moto><id>1</id><brand>Toyota</brand><model>Camry</model></Moto>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "1")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 OK и нужным авто")
    public void testGetUnknownVehicleById() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Car><id>1</id><brand>Toyota</brand><model>Camry</model></Car>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "1")
                        .param("databaseName", "xxx")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 OK и нужным авто")
    public void testGetEmptyDataBaseParameterVehicleById() throws Exception {

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
        String expectedMessage = "<message>Ничего не найдено по id = 999 в базе данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "999")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 ОК при отсутствии машины")
    public void testGetNonExistentMotoById() throws Exception {
        String expectedMessage = "<message>Ничего не найдено по id = 999 в базе данных moto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "999")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 ОК при отсутствии машины")
    public void testGetNonExistentVehicleById() throws Exception {
        String expectedMessage = "<message>Ничего не найдено по id = 999 в базе данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .param("id", "999")
                        .param("databaseName", "hz")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("GET / возвращает HTTP-ответ со статусом 200 ОК при отсутствии машины")
    public void testGetEmptyDataBaseParameterNonExistentCarById() throws Exception {
        String expectedMessage = "<message>Ничего не найдено по id = 999 в базе данных auto</message>";

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
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK и нужными автомобилями")
    public void testGetMotosByBrand() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Motos><Moto><id>1</id><brand>Toyota</brand><model>Camry</model></Moto>" +
                "<Moto><id>2</id><brand>Toyota</brand><model>Corolla</model></Moto></Motos>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "Toyota")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK и нужными автомобилями")
    public void testGetUnknownVehiclesByBrand() throws Exception {

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<Cars><Car><id>1</id><brand>Toyota</brand><model>Camry</model></Car>" +
                "<Car><id>2</id><brand>Toyota</brand><model>Corolla</model></Car></Cars>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "Toyota")
                        .param("databaseName", "hz")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK и нужными автомобилями")
    public void testGetNonExistentVehiclesByBrand() throws Exception {

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

        String expectedXml = "<message>Ничего не найдено по бренду qqq в базе данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "qqq")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK при отсутствии машин нужного бренда")
    public void testGetNonExistentMotosByBrand() throws Exception {

        String expectedXml = "<message>Ничего не найдено по бренду qqq в базе данных moto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "qqq")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK при отсутствии машин нужного бренда")
    public void testGetNonExistentUnknownVehiclesByBrand() throws Exception {

        String expectedXml = "<message>Ничего не найдено по бренду qqq в базе данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "qqq")
                        .param("databaseName", "hz")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedXml));
    }

    @Test
    @DisplayName("GET /brand возвращает HTTP-ответ со статусом 200 OK при отсутствии машин нужного бренда")
    public void testGetEmptyDataBaseParameterNonExistentVehiclesByBrand() throws Exception {

        String expectedMessage = "<message>Ничего не найдено по бренду qqq в базе данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.get("/brand")
                        .param("brand", "qqq")
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / добавляет автомобиль и возвращает сообщение о добавлении")
    public void testAddVehicleCar() throws Exception {
        Car car = new Car(4L, "Toyota", "Camry");
        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / добавляет автомобиль и возвращает сообщение о добавлении")
    public void testAddVehicleMoto() throws Exception {
        Car car = new Car(4L, "Toyota", "Camry");
        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись была добавлена в базу данных moto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / добавляет автомобиль и возвращает сообщение о добавлении")
    public void testAddUnknownVehicle() throws Exception {
        Car car = new Car(4L, "Toyota", "Camry");
        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "hz")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / добавляет автомобиль и возвращает сообщение о добавлении")
    public void testAddEmptyDataBaseParameterVehicle() throws Exception {
        Car car = new Car(4L, "Toyota", "Camry");
        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / не добавляет автомобиль и возвращает сообщение об ошибке")
    public void testBadRequestAddVehicleCar() throws Exception {
        Car car = new Car(4L, "ToyotaToyotaToyotaToyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись не была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "auto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / не добавляет автомобиль и возвращает сообщение об ошибке")
    public void testBadRequestAddVehicleMoto() throws Exception {
        Car car = new Car(4L, "ToyotaToyotaToyotaToyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись не была добавлена в базу данных moto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "moto")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / не добавляет автомобиль и возвращает сообщение об ошибке")
    public void testBadRequestAddUnknownVehicle() throws Exception {
        Car car = new Car(4L, "ToyotaToyotaToyotaToyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись не была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .param("databaseName", "hz")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

    @Test
    @DisplayName("POST / не добавляет автомобиль и возвращает сообщение об ошибке")
    public void testBadRequestAddEmptyDataBaseParameter() throws Exception {
        Car car = new Car(4L, "ToyotaToyotaToyotaToyota", "Camry");

        String carJson = objectMapper.writeValueAsString(car);

        String expectedMessage = "<message>Запись не была добавлена в базу данных auto</message>";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(carJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().xml(expectedMessage));
    }

}
