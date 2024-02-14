package com.SSS.restApi.xmlWrapper;

import com.SSS.restApi.models.Car;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JacksonXmlRootElement(localName = "Cars")
public class CarListResponse {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Car")
    private List<Car> car;

    public CarListResponse(List<Car> cars) {
        this.car = cars;
    }

}
