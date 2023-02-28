package com.jhlubucek.smart.controls.entity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Light {

    @GeneratedValue
    private int id;
    private String name;
    private String topicState;
    private String topicBrightness;
    private int brightnessMaxValue;
    private int brightnessMinValue;
    private boolean lastState;
    private int lastBrightness;
}
