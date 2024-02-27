package com.SSS.restApi.xmlWrapper.soap;

import com.SSS.restApi.models.moto.Moto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "Motos")
public class SoapMotoListResponse {
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
