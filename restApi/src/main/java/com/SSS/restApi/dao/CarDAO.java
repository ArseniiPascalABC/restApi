package com.sss.restapi.dao;

import com.sss.restapi.models.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class CarDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public CarDAO(@Qualifier("carDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public void save(Vehicle vehicle) {
        String sql = "INSERT INTO cars (brand, model) VALUES (?, ?)";
        jdbcTemplate.update(sql, vehicle.getBrand(), vehicle.getModel());
    }
}
