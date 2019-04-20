package com.leo.solace.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * @author Leo Liu
 * @create 20/04/2019
 */
@Slf4j
@Configuration
public class SolaceConfig {

  @Autowired private ConnectionFactory connectionFactory;


  /**
   * 配置jms的ContainerFactory，监听时需要指定该ContainerFactory
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


}
