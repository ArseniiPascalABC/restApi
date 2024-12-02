package com.sss.restapi.models.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sss.restapi.models.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement
@Data
public class Car implements Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    @JsonProperty("id")
    private Long id;
    @Column(length = 20)
    @JsonProperty("brand")
    private String brand;
    @Column(length = 100)
    @JsonProperty("model")
    private String model;

}
