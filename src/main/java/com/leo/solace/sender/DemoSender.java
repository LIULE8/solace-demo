package com.leo.solace.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessageToTopic(String destination, String message) {
        log.info("Sending event to topic: " + destination);
        jmsTemplate.convertAndSend(destination, message);
    }
}
