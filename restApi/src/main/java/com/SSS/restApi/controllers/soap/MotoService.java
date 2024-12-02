package com.sss.restapi.controllers.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sss.restapi.models.moto.Moto;
import com.sss.restapi.responses.soap.MotoResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@WebService(targetNamespace = "http://service.ws.sample/", name = "MotoService")
public interface MotoService {
    @WebResult(name = "motoById")
    @RequestWrapper(
            localName = "getMotoById",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestGetMotoById")
    @WebMethod(action = "urn:GetMotoById")
    @ResponseWrapper(
            localName = "getMotoByIdResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.getMotoByIdResponse"
    )
    MotoResponse getVehicleById(@WebParam(name = "id") Long id)
            throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException;

    @WebResult(name = "motosByBrand")
    @RequestWrapper(
            localName = "getMotosByBrand",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestGetMotosByBrand")
    @WebMethod(action = "urn:GetMotosByBrand")
    @ResponseWrapper(
            localName = "getMotosByBrandResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.getMotosByBrandResponse"
    )
    MotoResponse getVehiclesByBrand(@WebParam(name = "brand") String brand)
            throws JAXBException, JsonProcessingException;

    @WebResult(name = "moto")
    @RequestWrapper(
            localName = "addMoto",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestAddMoto")
    @WebMethod(action = "urn:AddMoto")
    @ResponseWrapper(
            localName = "addMotoResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.addMotoResponse"
    )
    MotoResponse addVehicle(@WebParam(name = "moto") Moto moto) throws JsonProcessingException;
}
