package com.jhlubucek.smart.controls.entity.rowMapper;

import com.jhlubucek.smart.controls.entity.SensorReading;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SensorReadingRowMapper implements RowMapper<SensorReading> {

    @Override
    public SensorReading mapRow(ResultSet rs, int rowNum) throws SQLException {
        SensorReading sensorReading = new SensorReading();
        sensorReading.setId(rs.getInt("id"));
        sensorReading.setSensorId(rs.getInt("sensor_id"));
        sensorReading.setValue(rs.getDouble("value"));
        sensorReading.setTime(rs.getTimestamp("time").toLocalDateTime());
        return sensorReading;
    }
}

