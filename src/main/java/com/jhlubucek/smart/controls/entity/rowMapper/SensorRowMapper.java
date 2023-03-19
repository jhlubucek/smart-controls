package com.jhlubucek.smart.controls.entity.rowMapper;

import com.jhlubucek.smart.controls.entity.Sensor;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SensorRowMapper implements RowMapper<Sensor> {
    @Override
    public Sensor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sensor sensor = new Sensor();
        sensor.setId(rs.getInt("id"));
        sensor.setDashboardId(rs.getInt("dashboard_id"));
        sensor.setName(rs.getString("name"));
        sensor.setTopic(rs.getString("topic"));
        sensor.setUnit(rs.getString("unit"));
        return sensor;
    }
}

