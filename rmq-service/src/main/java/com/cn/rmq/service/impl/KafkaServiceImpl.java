package com.cn.rmq.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.service.IReliableMessageService;
import com.cn.rmq.dal.mapper.MessageMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息服务实现
 * 
 * @author xianping_lu
 *
 */
@Slf4j
@ConditionalOnProperty(name = "spring.rmq", havingValue = "kafka", matchIfMissing = false)
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class KafkaServiceImpl extends BaseServiceImpl<MessageMapper, Message, String>
    implements IReliableMessageService {

    @Override
    public String createPreMessage(String consumerQueue, String messageBody) {
        return null;
    }

    @Override
    public void confirmAndSendMessage(String queue, String messageId) {

    }

    @Override
    public void deleteMessage(String queue, String messageId) {
        
        log.info("delete queue:{}, message id:{}, succesed ", queue, messageId);
    }
}
