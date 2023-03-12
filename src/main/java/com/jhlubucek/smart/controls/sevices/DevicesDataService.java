package com.jhlubucek.smart.controls.sevices;

import com.jhlubucek.smart.controls.entity.Light;
import com.jhlubucek.smart.controls.entity.Sensor;
import com.jhlubucek.smart.controls.entity.rowMapper.LightRowMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
public class DevicesDataService {
    @Autowired
    private MqttService mqttService;

    @Autowired
    private DatabaseConnector databaseConnector;

    @Scheduled(fixedDelayString = "10000")
    @PostConstruct
    public void subscribeResubscribe() throws MqttException {
        if (!mqttService.isConnected()){
            mqttService.connect();
        }
        System.out.println("pica ale uz");
        log.info("resubscribeing");
        List<Light> lights = databaseConnector.getAllLights();

        lights.forEach(
                light -> {
                    try {
                        log.info("subscribe to: " + light.getTopicState());
                        mqttService.subscribe(light.getTopicState(), (tpic, msg) -> {
                            String payload = new String(msg.getPayload());
                            Light l = databaseConnector.getLightByStateTopic(tpic);
                            databaseConnector.updateCurrentState(l.getId(), payload.equals("1"));
                        });

                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        log.info("subscribe to: " + light.getTopicBrightness());
                        mqttService.subscribe(light.getTopicBrightness(), (tpic, msg) -> {
                            String payload = new String(msg.getPayload());
                            Light l = databaseConnector.getLightByBrightnessTopic(tpic);
                            System.out.println(payload);
                            databaseConnector.updateCurrentBrightness(l.getId(), Integer.parseInt(payload));
                        });

                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        List<Sensor> sensors = databaseConnector.findAllSensors();
        sensors.forEach(sensor -> {
            try {
                log.info("subscribe to: " + sensor.getTopic());
                mqttService.subscribe(sensor.getTopic(), (tpic, msg) -> {
                    String str = new String(msg.getPayload());
                    double dbl = Double.parseDouble(str);

                    Sensor s = databaseConnector.getSensorByTopic(tpic);
                    System.out.println(String.format("payload: %s",dbl));
                    databaseConnector.saveReading(dbl, s.getId());
                    System.out.println("send");
                });
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        });


    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

}
