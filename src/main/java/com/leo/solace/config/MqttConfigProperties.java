package com.leo.solace.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "solace.mqtt")
public class MqttConfigProperties {

    /**
     * 订阅的bean名称
     */
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
    /**
     * 发布的bean名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    @Value("${url}")
    private String url;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${producer.client-id}")
    private String producerClientId;

    @Value("${producer.default-topic}")
    private String producerDefaultTopic;

    @Value("${consumer.client-id}")
    private String consumerClientId;

    @Value("${consumer.default-topic}")
    private String consumerDefaultTopic;
}
