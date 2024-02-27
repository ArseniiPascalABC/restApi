package com.SSS.restApi.xmlWrapper.soap;

import com.SSS.restApi.models.car.Car;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "Motos")
public class SoapCarListResponse {
    private List<Car> car;

    public SoapCarListResponse() {
    }

    public SoapCarListResponse(List<Car> car) {
        this.car = car;
    }

    @XmlElement(name = "Moto")
    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }
}
