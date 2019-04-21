package com.leo.solace.controller;

import com.leo.solace.mqtt.sender.IMqttSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqtt")
public class MqttController {

    @Autowired
    private IMqttSender iMqttSender;

    /**
     * 发送MQTT消息
     */
    @GetMapping("send")
    public void sendMqtt(@RequestParam("msg") String message) {
        iMqttSender.sendToMqtt(message);
    }
}
