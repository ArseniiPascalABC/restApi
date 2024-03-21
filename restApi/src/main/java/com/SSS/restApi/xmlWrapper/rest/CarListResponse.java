package com.sss.restapi.xmlwrapper.rest;

import com.sss.restapi.models.car.Car;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JacksonXmlRootElement(localName = "Cars")
//добавить карс в свагер
public class CarListResponse {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Car")
    private List<Car> car;

    public CarListResponse(List<Car> cars) {
        this.car = cars;
    }

}
