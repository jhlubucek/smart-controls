package com.jhlubucek.smart.controls.entity.rowMapper;

import com.jhlubucek.smart.controls.entity.Dashboard;
import com.jhlubucek.smart.controls.entity.SensorReading;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardRowMapper implements RowMapper<Dashboard> {

    @Override
    public Dashboard mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dashboard dashboard = new Dashboard();
        dashboard.setId(rs.getInt("id"));
        dashboard.setName(rs.getString("name"));
        return dashboard;
    }
}

