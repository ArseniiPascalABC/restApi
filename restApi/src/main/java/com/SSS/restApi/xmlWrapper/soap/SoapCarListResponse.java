package com.SSS.restApi.xmlWrapper.soap;

import com.SSS.restApi.models.car.Car;
import jakarta.xml.bind.annotation.*;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "Cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapCarListResponse {
    @XmlElement(name = "Car")
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

}
