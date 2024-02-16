package com.SSS.restApi.models;

import org.springframework.stereotype.Component;

@Component

public interface Vehicle {
    Long getId();
    String getBrand();

    String getModel();
}
