package com.jhlubucek.smart.controls.sevices;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final IMqttClient mqttClient;
    private final MqttConnectOptions mqttConnectOptions;

    public MqttService(IMqttClient mqttClient, MqttConnectOptions mqttConnectOptions) {
        this.mqttClient = mqttClient;
        this.mqttConnectOptions = mqttConnectOptions;
    }


    public void subscribe(final String topic) throws MqttException, InterruptedException {
        System.out.println("Messages received:");

        mqttClient.subscribeWithResponse(topic, (tpic, msg) -> {
            System.out.println(msg.getId() + " -> " + new String(msg.getPayload()));
        });
    }

    public void subscribe(String var1, IMqttMessageListener var2) throws MqttException {
        mqttClient.subscribe(var1, var2);
    }

    public void publish(final String topic, final String payload, int qos, boolean retained)
            throws MqttPersistenceException, MqttException {

        if (!mqttClient.isConnected()){
            mqttClient.connect(mqttConnectOptions);
        }
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);

        mqttClient.publish(topic, mqttMessage);

        //mqttClient.publish(topic, payload.getBytes(), qos, retained);

//        mqttClient.disconnect();
    }

    @PostConstruct
    public void sub() throws MqttException, InterruptedException {
        System.out.println("pica ale uz");
        if (!mqttClient.isConnected()){
            mqttClient.connect(mqttConnectOptions);
        }
        this.subscribe("myTopic");
        this.publish("/test/topic", "hello from java", 0, false);
    }

    public boolean isConnected(){
        return mqttClient.isConnected();
    }

    public void connect() throws MqttException {
        mqttClient.connect(mqttConnectOptions);
    }
}
