package com.cn.rmq.service.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Value("${spring.rmq.rocketmq.name-serv}")
    private String nameServ;
    @Value("${spring.rmq.rocketmq.producer-group}")
    private String producerGroup;

    @Bean
    public DefaultMQProducer producer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServ);
        producer.setProducerGroup(producerGroup);
        producer.start();
        return producer;
    }
}
