package com.example.demo;

import com.example.demo.Car;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends R2dbcRepository<Car, Long> {
}