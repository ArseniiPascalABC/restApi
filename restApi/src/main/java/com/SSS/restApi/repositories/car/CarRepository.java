package com.SSS.restApi.repositories.car;

import com.SSS.restApi.models.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByBrandIgnoreCase(String brand);
}
