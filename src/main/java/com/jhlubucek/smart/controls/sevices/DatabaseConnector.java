package com.jhlubucek.smart.controls.sevices;

import com.jhlubucek.smart.controls.entity.Dashboard;
import com.jhlubucek.smart.controls.entity.Light;
import com.jhlubucek.smart.controls.entity.Sensor;
import com.jhlubucek.smart.controls.entity.SensorReading;
import com.jhlubucek.smart.controls.entity.rowMapper.DashboardRowMapper;
import com.jhlubucek.smart.controls.entity.rowMapper.LightRowMapper;
import com.jhlubucek.smart.controls.entity.rowMapper.SensorReadingRowMapper;
import com.jhlubucek.smart.controls.entity.rowMapper.SensorRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DatabaseConnector {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    final LightRowMapper lightRowMapper =  new LightRowMapper();
    final SensorRowMapper sensorRowMapper =  new SensorRowMapper();
    final SensorReadingRowMapper sensorReadingRowMapper =  new SensorReadingRowMapper();

    public List<Light> getAllLights(){
        List<Light> lights = jdbcTemplate.query("SELECT * FROM light", lightRowMapper);
        return lights;
    }

    public List<Light> getAllLightsFromDashboard(int dashboardId){
        List<Light> lights = jdbcTemplate.query(
                "SELECT * FROM light WHERE dashboard_id = ?",
                lightRowMapper,
                dashboardId);
        return lights;
    }

    public void updateCurrentState(int lightId, boolean state) {
        String sql = "UPDATE light SET current_state = ? WHERE id = ?";
        jdbcTemplate.update(sql, state ? 1 : 0, lightId);
    }

    public void updateCurrentBrightness(int lightId, int brightness) {
        String sql = "UPDATE light SET current_brightness = ? WHERE id = ?";
        jdbcTemplate.update(sql, brightness, lightId);
    }

    public Light getLightByStateTopic(String stateTopic) {
        String sql = "SELECT * FROM light WHERE topic_state = ?";
        return jdbcTemplate.queryForObject(sql, lightRowMapper, stateTopic);
    }

    public Light getLightByBrightnessTopic(String brightnessTopic) {
        String sql = "SELECT * FROM light WHERE topic_brightness = ?";
        return jdbcTemplate.queryForObject(sql, lightRowMapper, brightnessTopic);
    }

    public Light getLightById(int id) {
        String sql = "SELECT * FROM light WHERE id = ?";
        List<Light> lights = jdbcTemplate.query(sql, lightRowMapper, id);
        return lights.size() > 0 ? lights.get(0) : null;
    }

    public Sensor getSensorById(int id) {
        String sql = "SELECT * FROM sensor WHERE id = ?";
        List<Sensor> sensors = jdbcTemplate.query(sql, new SensorRowMapper(), id);
        return sensors.size() > 0 ? sensors.get(0) : null;
    }

    public void saveLight(Light light) {
        jdbcTemplate.update("INSERT INTO light (dashboard_id, name, topic_state, topic_brightness, min_brightness, max_brightness, current_state, current_brightness) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                light.getDashboardId(),
                light.getName(),
                light.getTopicState(),
                light.getTopicBrightness(),
                light.getMinBrightness(),
                light.getMaxBrightness(),
                light.isCurrentState() ? 1 : 0,
                light.getCurrentBrightness());
    }

    public void saveSensor(Sensor sensor) {
        jdbcTemplate.update("INSERT INTO `sensor` (`name`, `topic`, `unit`, `dashboard_id`) VALUES (?, ?, ?, ?);",
                sensor.getName(),
                sensor.getTopic(),
                sensor.getUnit(),
                sensor.getDashboardId());
    }

    public void saveDashboard(Dashboard dashboard) {
        jdbcTemplate.update("INSERT INTO `dashboard` (`name`) VALUES (?);",
                dashboard.getName());
    }

    public void updateLight(Light light) {
        jdbcTemplate.update("UPDATE light SET name = ?, topic_state = ?, topic_brightness = ?, min_brightness = ?, max_brightness = ?, current_state = ?, current_brightness = ? WHERE id = ?",
                light.getName(),
                light.getTopicState(),
                light.getTopicBrightness(),
                light.getMinBrightness(),
                light.getMaxBrightness(),
                light.isCurrentState() ? 1 : 0,
                light.getCurrentBrightness(),
                light.getId());
    }

    //this only works with low traffic
    public int getLastInsertedId() {
        Long lastInsertedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return lastInsertedId.intValue();
    }

    public void deleteLight(int id) {
        jdbcTemplate.update("DELETE FROM light WHERE id = ?", id);
    }

    public void deleteSensor(int id) {
        jdbcTemplate.update("DELETE FROM sensor WHERE id = ?", id);
    }

    public void deleteDashboard(int id) {
        jdbcTemplate.update("DELETE FROM dashboard WHERE id = ? AND (SELECT COUNT(*) FROM dashboard) > 1", id);
    }

    public List<Sensor> findAllSensors() {
        String sql = "SELECT * FROM sensor";
        return jdbcTemplate.query(sql, new SensorRowMapper());
    }

    public List<Sensor> findAllSensorsFromDashboard(int dashboardId) {
        String sql = "SELECT * FROM sensor WHERE dashboard_id = ?";
        return jdbcTemplate.query(sql, new SensorRowMapper(), dashboardId);
    }

    public void deleteSensorById(int id) {
        String sql = "DELETE FROM sensor WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Sensor getSensorByTopic(String topic) {
        String sql = "SELECT * FROM sensor WHERE topic = ?";
        return jdbcTemplate.queryForObject(sql, sensorRowMapper, topic);
    }

    public void updateSensor(Sensor sensor) {
        String sql = "UPDATE sensor SET name = ?, topic = ?, unit = ? WHERE id = ?";
        jdbcTemplate.update(sql, sensor.getName(), sensor.getTopic(), sensor.getUnit(), sensor.getId());
    }

    public List<SensorReading> getReadingsForLast24Hours(int sensorId) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime twentyFourHoursAgo = now.minusHours(24);
//        System.out.println(now.format(DateTimeFormatter.ISO_DATE_TIME));
//        System.out.println(twentyFourHoursAgo.format(DateTimeFormatter.ISO_DATE_TIME));

        String sql = "SELECT id, sensor_id, value, time FROM sensor_reading WHERE sensor_id = ? AND time > DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
        return jdbcTemplate.query(sql, new SensorReadingRowMapper(), sensorId);
    }

    public void saveReading(double value, int sensorId) {
        String sql = "INSERT INTO sensor_reading (sensor_id, value) VALUES (?, ?)";
        jdbcTemplate.update(sql, sensorId, value);
    }

    public List<Dashboard> findAllDashboards() {
        String sql = "SELECT * FROM dashboard";
        return jdbcTemplate.query(sql, new DashboardRowMapper());
    }

    public Dashboard getDashboardById(int id) {
        String sql = "SELECT * FROM dashboard WHERE id = ?";
        List<Dashboard> dashboards = jdbcTemplate.query(sql, new DashboardRowMapper(), id);
        return dashboards.size() > 0 ? dashboards.get(0) : null;
    }

    public Dashboard getMainDashboard() {
        String sql = "SELECT * FROM dashboard ORDER BY id ASC LIMIT 1";
        List<Dashboard> dashboards = jdbcTemplate.query(sql, new DashboardRowMapper());
        return dashboards.size() > 0 ? dashboards.get(0) : null;
    }

    public void updateDashboard(Dashboard dashboard) {
        String sql = "UPDATE dashboard SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, dashboard.getName(), dashboard.getId());
    }
}
