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
    private int minBrightness;
    private int maxBrightness;
    private boolean currentState;
    private int currentBrightness;
    private int dashboardId;

}
