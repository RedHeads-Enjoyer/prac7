package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("cars")
public class Car {
    @Id
    private Long id;
    @Column("model")
    private String model;
    @Column("color")
    private String color;
    @Column("price")
    private Float price;
}