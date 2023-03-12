package com.jhlubucek.smart.controls.entity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorReading {

    @GeneratedValue
    private int id;
    private int sensorId;
    private double value;
    private LocalDateTime time;

    public String getFormatedDate(){
        return time.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
