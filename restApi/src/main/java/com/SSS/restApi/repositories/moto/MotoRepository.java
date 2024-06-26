package com.sss.restapi.repositories.moto;

import com.sss.restapi.models.moto.Moto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("carRepository1")
public interface MotoRepository extends JpaRepository<Moto, Long> {
    List<Moto> findAllByBrandIgnoreCase(String brand);
}
