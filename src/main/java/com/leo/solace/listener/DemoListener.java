package com.leo.solace.listener;

import com.leo.solace.dto.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Slf4j
public class DemoListener {

    private static final String DEFAULT_CONCURRENCY = "3";
    private static final String QUEUE_LISTENER_FACTORY = "queueListenerFactory";

    @JmsListener(
            destination = "demo-queue",
            containerFactory = QUEUE_LISTENER_FACTORY,
            concurrency = DEFAULT_CONCURRENCY
    )
    public void process(@Validated @Payload MessageRequest messageRequest) {
        System.out.println("name: " + messageRequest.getName());
        System.out.println("coded: " + messageRequest.getCode());
    }
}
