package com.damon.rmq.service.impl;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;

import com.alibaba.fastjson.JSONObject;
import com.damon.rmq.api.enums.MessageStatusEnum;
import com.damon.rmq.api.exceptions.CheckException;
import com.damon.rmq.api.exceptions.RmqException;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.TransactionMessage;
import com.damon.rmq.api.model.po.Message;
import com.damon.rmq.api.model.vo.AdminMessageVo;
import com.damon.rmq.api.service.IReliableMessageService;
import com.damon.rmq.dal.mapper.MessageMapper;

import cn.hutool.core.util.IdUtil;
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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public String createPreMessage(String consumerQueue, String messageBody) {
        if (StringUtils.isBlank(consumerQueue)) {
            throw new CheckException("consumerQueue is empty");
        }
        if (StringUtils.isBlank(messageBody)) {
            throw new CheckException("messageBody is empty");
        }

        String id = IdUtil.simpleUUID();
        // 插入预发送消息记录
        Message message = new Message();
        message.setId(id);
        message.setConsumerQueue(consumerQueue);
        message.setMessageBody(messageBody);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        mapper.insertSelective(message);
        
        log.debug("create pre message succeed. queue : {}, message id : {}, body : {}", consumerQueue, id, messageBody);
        return id;
    }

    @Override
    public void confirmAndSendMessage(String queue, String messageId) {
        
        if (StringUtils.isBlank(messageId)) {
            throw new CheckException("messageId is empty");
        }

        // 获取消息
        AdminMessageVo message = mapper.getMessage(queue, messageId);
        if (message == null) {
            throw new CheckException("message not exist, queue :" + queue + ", message id : " + messageId);
        }

        // 发送MQ消息
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setMessageId(messageId);
        transactionMessage.setMessageBody(message.getMessageBody());

        String body = JSONObject.toJSONString(transactionMessage);
        try {
            kafkaTemplate.send(queue, body).get();
            mapper.updateMessageStatus(queue, messageId, MessageStatusEnum.SENDING.getValue());
            log.debug("send message to kafka succeed. queue: {}, message id : {}, body : {} ", queue, messageId, body);
        } catch (Exception e) {
            log.error("send message to kafka failed ", e);
            mapper.updateMessageStatus(queue, messageId, MessageStatusEnum.SEND_FAILED.getValue());
            throw new RmqException("send message to kafka failed", e);
        }
    }

    @Override
    public void directSendMessage(String queue, String messageId, String messageBody) {

        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setMessageId(messageId);
        transactionMessage.setMessageBody(messageBody);

        String body = JSONObject.toJSONString(transactionMessage);

        try {
            kafkaTemplate.send(queue, body).get();
            log.debug("send message to kafka succeed. queue: {}, message id : {}, body : {} ", queue, messageId, body);
        } catch (Exception e) {
            log.error("send message to kafka failed ", e);
            throw new RmqException("send message to kafka failed ", e);
        }
    }

    @Override
    public void deleteMessage(String queue, String messageId) {
        int count = mapper.deleteMessage(queue, messageId);
        if (count > 0) {
            log.debug("delete message succeed. queue: {}, message id : {}, body : {} ", queue, messageId);
        } else {
            log.error("delete message failed. queue: {}, message id : {}, body : {} ", queue, messageId);
        }
    }
}
