package com.leo.solace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leo.solace.demo.sender.DemoSender;
import com.leo.solace.dto.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoSender demoSender;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("send")
    public void sendMessage(@RequestBody MessageRequest messageRequest) throws JsonProcessingException {
        demoSender.sendMessageToTopic("demo-topic", objectMapper.writeValueAsString(messageRequest));
    }
}
