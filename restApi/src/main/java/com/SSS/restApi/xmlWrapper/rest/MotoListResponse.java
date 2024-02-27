package com.SSS.restApi.xmlWrapper.rest;

import com.SSS.restApi.models.moto.Moto;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JacksonXmlRootElement(localName = "Motos")
public class MotoListResponse {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Moto")
    private List<Moto> moto;

    public MotoListResponse(List<Moto> moto) {
        this.moto = moto;
    }

}
