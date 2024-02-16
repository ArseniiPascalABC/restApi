package com.SSS.restApi.models.moto;

import com.SSS.restApi.models.Vehicle;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "motos")
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement
@Data
public class Moto implements Vehicle{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @Column(length = 20)
    private String brand;
    @Column(length = 100)
    private String model;

}
