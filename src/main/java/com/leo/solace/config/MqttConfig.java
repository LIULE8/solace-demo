package com.leo.solace.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import static com.leo.solace.config.MqttConfigProperties.CHANNEL_NAME_IN;
import static com.leo.solace.config.MqttConfigProperties.CHANNEL_NAME_OUT;

@Slf4j
@Configuration

public class MqttConfig {

    @Autowired private MqttConfigProperties mqttConfigProperties;

    /**
     * MQTT连接器选项
     *
     * @return
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttConfigProperties.getUsername());
        mqttConnectOptions.setPassword(mqttConfigProperties.getPassword().toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{mqttConfigProperties.getUrl()});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }

    /**
     * MQTT客户端
     *
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    /**
     * MQTT信息通道（生产者）
     *
     * @return
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttConfigProperties.getProducerClientId(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttConfigProperties.getProducerDefaultTopic());
        return messageHandler;
    }

    /**
     * MQTT信息通道（消费者）
     *
     * @return
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息订阅绑定（消费者）
     *
     * @return
     */
    @Bean
    public MessageProducer inbound() {
        // 可以同时消费（订阅）多个Topic
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttConfigProperties.getConsumerClientId(), mqttClientFactory(),
                        StringUtils.split(mqttConfigProperties.getConsumerDefaultTopic(), ","));
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    /**
     * MQTT消息处理器（消费者）
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return message -> log.error("============{}============", message.getPayload());
    }

}
