package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.dao.MotoDAO;
import com.SSS.restApi.models.moto.Moto;
import com.SSS.restApi.repositories.moto.MotoRepository;
import com.SSS.restApi.responses.soap.MotoResponse;
import com.SSS.restApi.xmlWrapper.MotoListResponse;
import jakarta.jws.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebService(
        serviceName = "MotoService",
        endpointInterface = "com.SSS.restApi.controllers.soap.MotoService")
public class MotoServiceImpl implements MotoService {

    private final MotoRepository motoRepository;
    private final MotoDAO motoDAO;
    @Override
    public Moto getVehicleById(Long id) {
        return motoRepository.findById(id).orElse(null);
    }

    @Override
    public MotoListResponse getVehiclesByBrand(String brand) {
        List<Moto> motos = motoRepository.findAllByBrandIgnoreCase(brand);
        if (motos.isEmpty()) {
            log.warn("MotoController getMotosByBrand, the motos the user was looking for were not found in database motos");
            return new MotoListResponse(new ArrayList<>());
        }
        MotoListResponse motoListResponse = new MotoListResponse(motos);
        log.info("MotoController getMotosByBrand, the motos the user was looking for were found");
        return motoListResponse;
    }

    @Override
    public MotoResponse addVehicle(Moto moto) {
        MotoResponse response = new MotoResponse();
        try {
            motoDAO.save(moto);
            log.info("MotoController addMoto, moto was added");
            response.setMessage("Запись добавлена");
            response.setSuccess(true);
        } catch (Exception e) {
            log.info("MotoController addMoto, moto was not added");
            response.setMessage("Запись не была добавлена");
            response.setSuccess(false);
        }
        return response;
    }
}
