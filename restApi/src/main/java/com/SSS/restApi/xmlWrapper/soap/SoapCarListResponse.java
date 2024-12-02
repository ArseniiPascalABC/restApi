package com.sss.restapi.xmlwrapper.soap;

import com.sss.restapi.models.car.Car;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "Cars")
@NoArgsConstructor
public class SoapCarListResponse {
    private List<Car> car;

    public SoapCarListResponse(List<Car> car) {
        this.car = car;
    }

    @XmlElement(name = "Car")
    public List<Car> getCar() {
        return car;
    }

}
