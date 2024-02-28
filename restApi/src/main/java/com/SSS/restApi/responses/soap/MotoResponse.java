package com.SSS.restApi.responses.soap;

import com.SSS.restApi.xmlWrapper.soap.SoapMotoListResponse;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlSeeAlso(SoapMotoListResponse.class)
public class MotoResponse {
    private Object data;
    private String message;
    private boolean success;
}
