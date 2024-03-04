package com.SSS.restApi.controllers.soap;

import com.SSS.restApi.models.car.Car;
import com.SSS.restApi.responses.soap.CarResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@WebService(targetNamespace = "http://service.ws.sample/", name = "CarService")
public interface CarService {
    @WebResult(name = "carById")
    @RequestWrapper(
            localName = "getCarById",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestGetCarById")
    @WebMethod(action = "urn:GetCarById")
    @ResponseWrapper(
            localName = "getCarByIdResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.getCarByIdResponse"
    )
    CarResponse getVehicleById(@WebParam(name = "id") Long id) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException;

    @WebResult(name = "carsByBrand")
    @RequestWrapper(
            localName = "getCarsByBrand",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestGetCarsByBrand")
    @WebMethod(action = "urn:GetCarsByBrand")
    @ResponseWrapper(
            localName = "getCarsByBrandResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.getCarsByBrandResponse"
    )
    CarResponse getVehiclesByBrand(@WebParam(name = "brand") String brand) throws JAXBException, ExecutionException, InterruptedException, TimeoutException, JsonProcessingException;

    @WebResult(name = "car")
    @RequestWrapper(
            localName = "addCar",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.RequestAddCar")
    @WebMethod(action = "urn:AddCar")
    @ResponseWrapper(
            localName = "addCarResponse",
            targetNamespace = "http://service.ws.sample/",
            className = "sample.ws.service.addCarResponse"
    )
    CarResponse addVehicle(@WebParam(name = "car") Car car) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException;
}
