package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.responses.soap.MotoResponse;
import com.SSS.restApi.services.soap.MotoServiceForKafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
@WebService(
        serviceName = "MotoService",
        endpointInterface = "com.SSS.restApi.controllers.soap.MotoService")
public class MotoServiceImpl implements MotoService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MotoServiceForKafka motoServiceForKafka;

    @Override
    public MotoResponse getVehicleById(Long id) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehicleById");
        jsonMessage.put("Body", id.toString());
        System.out.println(jsonMessage);
        kafkaTemplate.send("soapTopic", jsonMessage.toString());

        return motoServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
    }

    @Override
    public MotoResponse getVehiclesByBrand(String brand) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("Vehicle", "Moto");
        jsonMessage.put("Method", "getVehiclesByBrand");
        jsonMessage.put("Body", brand);
        System.out.println(jsonMessage);
        kafkaTemplate.send("soapTopic", jsonMessage.toString());

        return motoServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
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
        kafkaTemplate.send("soapTopic", jsonMessage.toString());

        return motoServiceForKafka.processMessageAndGetResponse(jsonMessage.toString());
    }
}
