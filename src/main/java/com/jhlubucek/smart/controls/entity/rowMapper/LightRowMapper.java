package com.jhlubucek.smart.controls.entity.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jhlubucek.smart.controls.entity.Light;
import org.springframework.jdbc.core.RowMapper;

public class LightRowMapper implements RowMapper<Light> {

    @Override
    public Light mapRow(ResultSet rs, int rowNum) throws SQLException {
        Light light = new Light();
        light.setId(rs.getInt("id"));
        light.setDashboardId(rs.getInt("dashboard_id"));
        light.setName(rs.getString("name"));
        light.setTopicState(rs.getString("topic_state"));
        light.setTopicBrightness(rs.getString("topic_brightness"));
        light.setMinBrightness(rs.getInt("min_brightness"));
        light.setMaxBrightness(rs.getInt("max_brightness"));
        light.setCurrentBrightness(rs.getInt("current_brightness"));
        light.setCurrentState(rs.getBoolean("current_state"));
        return light;
    }
}