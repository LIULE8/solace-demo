package com.leo.solace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.GenericMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.retry.annotation.EnableRetry;

import javax.jms.ConnectionFactory;
import java.util.Arrays;

/**
 * @author Leo Liu
 * @create 20/04/2019
 */
@Slf4j
@Configuration
@EnableJms
@EnableRetry
public class SolaceConfig implements JmsListenerConfigurer {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 配置jms的ContainerFactory，监听时需要指定该ContainerFactory
     *
     * @return
     */
    @Bean
    public DefaultJmsListenerContainerFactory queueListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    /**
     * 自定义JmsTemplate 设置PubSub为true 是发送到topic，为false则发送到queue
     *
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplate() {
        CachingConnectionFactory ccf = new CachingConnectionFactory(connectionFactory);
        JmsTemplate jmsTemplate = new JmsTemplate(ccf);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        DefaultMessageHandlerMethodFactory factory = handlerMethodFactory();
        registrar.setMessageHandlerMethodFactory(factory);
    }

    @Bean
    public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        MappingJackson2MessageConverter mappingJackson2MessageConverter =
                new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setObjectMapper(objectMapper);
        MessageConverter messageConverter =
                new CompositeMessageConverter(
                        Arrays.asList(new GenericMessageConverter(), mappingJackson2MessageConverter));
        factory.setMessageConverter(messageConverter);
        return factory;
    }

}
