package com.cn.rmq.service.mq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
/**
 * kafka 配置
 * 
 * @author xianpinglu
 *
 */
@Configuration
@ConditionalOnProperty(name = "spring.rmq", havingValue = "kafka", matchIfMissing = false)
public class KafkaConfig {


    
}
