package com.SSS.restApi.models.kafka;

import com.SSS.restApi.models.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {
    @NonNull
    String name;
    String id;
    String brand;
    Vehicle vehicle;
}
