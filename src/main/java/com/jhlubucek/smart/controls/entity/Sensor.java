package com.jhlubucek.smart.controls.entity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

    @GeneratedValue
    private int id;
    private String name;
    private String topic;
    private String unit;
    private List<SensorReading> readings;
}
