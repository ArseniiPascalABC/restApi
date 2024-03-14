package com.sss.restapi.xmlwrapper.soap;

import com.sss.restapi.models.moto.Moto;
import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "Motos")
@NoArgsConstructor
public class SoapMotoListResponse {
    private List<Moto> moto;

    public SoapMotoListResponse(List<Moto> moto) {
        this.moto = moto;
    }

    @XmlElement(name = "Moto")
    public List<Moto> getMoto() {
        return moto;
    }

}
