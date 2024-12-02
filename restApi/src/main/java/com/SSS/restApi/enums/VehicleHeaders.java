package com.sss.restapi.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum VehicleHeaders {
    VEHICLE("vehicle"),
    ID("id"),
    BRAND("brand"),
    MODEL("model");

    private final String headerName;

    VehicleHeaders(String headerName) {
        this.headerName = headerName;
    }

    public static String[] getAllHeaders() {
        return Arrays.stream(values())
                     .map(VehicleHeaders::getHeaderName)
                     .toArray(String[]::new);
    }
}