package com.sss.restapi.models.moto;

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
@Table(name = "motos")
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement
@Data
public class Moto implements Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @Column(length = 20)
    private String brand;
    @Column(length = 100)
    private String model;

}
