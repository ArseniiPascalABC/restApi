package com.SSS.restApi.controllers.rest;

import com.SSS.restApi.controllers.soap.CarServiceImpl;
import com.SSS.restApi.responses.soap.CarResponse;
import com.SSS.restApi.services.rest.CarVehicleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest/cars")
public class CarVehicleController implements VehicleController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CarVehicleService carVehicleService;
    private final CarServiceImpl carService;
    @Override
    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehicleById(@RequestParam("id") Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());
    }
    @GetMapping(value = "/soap", produces = MediaType.APPLICATION_XML_VALUE)
    public CarResponse soapRequestForGetVehicleById(Long id) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        return carService.getVehicleById(id);
    }

    @GetMapping(value = "/soapRequest", produces = MediaType.APPLICATION_XML_VALUE)
    public StringBuilder soapRequestGetVehicleById(Long id){
        try {
            String soapRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.ws.sample/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <ser:getCarById>\n" +
                    "         <!--Optional:-->\n" +
                    "         <id>"+id+"</id>\n" +
                    "      </ser:getCarById>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            URL url = new URL("http://localhost:8080/Service/CarService");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(soapRequest.getBytes());
            outputStream.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("SOAP Response:\n" + response);
            return response;
        } catch (Exception e) {
            log.error("Ошибка в soap запросе из rest " + e);
            return new StringBuilder();
        }
    }


    @Override
    @GetMapping(value = "/brand", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> getVehiclesByBrand(@RequestParam("brand") String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());

    }

    @Override
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Object> addVehicle(@RequestBody String requestBody) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Car");
        jsonMessage.put("Method", "addVehicle");
        jsonMessage.put("Body", requestBody);
        kafkaTemplate.send("restTopic", jsonMessage.toString());

        return carVehicleService.processMessageAndGetResponse(jsonMessage.toString());

    }

}
