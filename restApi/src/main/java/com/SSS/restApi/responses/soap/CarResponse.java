package com.sss.restapi.responses.soap;

import com.sss.restapi.xmlwrapper.soap.SoapCarListResponse;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlSeeAlso(SoapCarListResponse.class)
public class CarResponse {
    private Object data;
    private String message;
    private boolean success;
}
