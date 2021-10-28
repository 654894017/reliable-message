package com.cn.rmq.service.impl;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.alibaba.fastjson.JSONObject;
import com.cn.rmq.api.enums.MessageStatusEnum;
import com.cn.rmq.api.exceptions.CheckException;
import com.cn.rmq.api.exceptions.RmqException;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.TransactionMessage;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.model.vo.AdminMessageVo;
import com.cn.rmq.api.service.IReliableMessageService;
import com.cn.rmq.dal.mapper.MessageMapper;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * RocketMQ消息服务实现
 * 
 * @author xianping_lu
 *
 */
@Slf4j
@ConditionalOnProperty(name = "spring.rmq", havingValue = "rocketmq", matchIfMissing = false)
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class RocketMQServiceImpl extends BaseServiceImpl<MessageMapper, Message, String>
    implements IReliableMessageService {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

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

        log.info("create pre message succesed. queue : {}, message id : {}, body : {}", consumerQueue, id, messageBody);
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
        org.apache.rocketmq.common.message.Message rmessage = //
            new org.apache.rocketmq.common.message.Message(message.getConsumerQueue(),
                body.getBytes(Charset.forName("UTF-8")));
        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
                mapper.updateMessageStatus(queue, messageId, MessageStatusEnum.SENDING.getValue());
                log.info("send message to rocketmq succesed. queue: {}, message id : {}, body : {} ", queue, messageId,
                    body);
            } else {
                log.error("send message to rocketmq failed, rocketmq return status code: {}.", result.getSendStatus());
                mapper.updateMessageStatus(queue, messageId, MessageStatusEnum.SEND_FAILED.getValue());
                throw new RmqException(
                    "send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            log.error("send message to rocketmq failed ", e);
            mapper.updateMessageStatus(queue, messageId, MessageStatusEnum.SEND_FAILED.getValue());
            throw new RmqException("send message to rocketmq failed", e);
        }
    }

    @Override
    public void directSendMessage(String consumerQueue, String messageId, String messageBody) {

        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setMessageId(messageId);
        transactionMessage.setMessageBody(messageBody);

        String body = JSONObject.toJSONString(transactionMessage);

        org.apache.rocketmq.common.message.Message rmessage =
            new org.apache.rocketmq.common.message.Message(consumerQueue, body.getBytes(Charset.forName("UTF-8")));
        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("send message to rocketmq failed, rocketmq return status code: {}.", result.getSendStatus());
                throw new RmqException(
                    "send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
            log.info("send message to rocketmq succesed. queue: {}, message id : {}, body : {}.", consumerQueue,
                messageId, body);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            log.error("send message to rocketmq failed ", e);
            throw new RmqException("send message to rocketmq failed ", e);
        }
    }

    @Override
    public void deleteMessage(String queue, String messageId) {
        int count = mapper.deleteMessage(queue, messageId);
        if (count > 0) {
            log.info("delete message succesed. queue: {}, message id : {}, body : {} ", queue, messageId);
        } else {
            log.error("delete message failed. queue: {}, message id : {}, body : {} ", queue, messageId);
        }
    }
}
