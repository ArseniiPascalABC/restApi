package com.SSS.restApi.xmlWrapper.soap;

import com.SSS.restApi.models.car.Car;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "Cars")
public class SoapCarListResponse {
    private List<Car> car;

    public SoapCarListResponse() {
    }

    public SoapCarListResponse(List<Car> car) {
        this.car = car;
    }

    @XmlElement(name = "Car")
    public List<Car> getCar() {
        return car;
    }

    public void setCar(List<Car> car) {
        this.car = car;
    }
}
