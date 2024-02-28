package com.SSS.restApi.xmlWrapper.soap;

import com.SSS.restApi.models.moto.Moto;
import jakarta.xml.bind.annotation.*;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "Motos")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapMotoListResponse {
    @XmlElement(name = "Moto")
    private List<Moto> moto;

    public SoapMotoListResponse() {
    }

    public SoapMotoListResponse(List<Moto> moto) {
        this.moto = moto;
    }

    @XmlElement(name = "Moto")
    public List<Moto> getMoto() {
        return moto;
    }

}
