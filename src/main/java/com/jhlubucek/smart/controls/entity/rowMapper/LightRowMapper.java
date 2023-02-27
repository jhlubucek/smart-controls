package com.jhlubucek.smart.controls.entity.rowMapper;

import com.jhlubucek.smart.controls.entity.Light;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightRowMapper implements RowMapper<Light> {

    @Override
    public Light mapRow(ResultSet rs, int rowNum) throws SQLException {
        Light light = new Light();
        light.setId(rs.getInt("id"));
        light.setTopicState(rs.getString("topic_state"));
        light.setTopicBrightness(rs.getString("topic_brightness"));
        light.setBrightnessMaxValue(rs.getInt("brightness_max_value"));
        light.setLastState(rs.getString("last_state"));
        return light;
    }
}
