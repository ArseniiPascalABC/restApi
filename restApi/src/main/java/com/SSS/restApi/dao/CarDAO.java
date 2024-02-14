package com.SSS.restApi.dao;

import com.SSS.restApi.models.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarDAO {
    private final JdbcTemplate jdbcTemplate;
    public void save(Car car) {
        String sql = "INSERT INTO cars (brand, model) VALUES (?, ?)";
        jdbcTemplate.update(sql, car.getBrand(), car.getModel());
    }
}
