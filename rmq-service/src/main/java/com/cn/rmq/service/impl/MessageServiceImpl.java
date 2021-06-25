package com.cn.rmq.service.impl;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cn.rmq.api.exceptions.CheckException;
import com.cn.rmq.api.exceptions.RmqException;
import com.cn.rmq.api.model.RmqMessage;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.service.IMessageService;
import com.cn.rmq.dal.mapper.MessageMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息服务实现
 *
 * @author Chen Nan
 * @date 2019/3/11.
 */
@Service
@Slf4j
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message, String> implements IMessageService {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public void resendMessage(Message message) {
        log.info("【resendMessage】start, messageId={}", message.getId());
        // 增加重发次数
        mapper.addResendTimes(message.getId());

        // 发送MQ消息
        RmqMessage rmqMessage = new RmqMessage();
        rmqMessage.setMessageId(message.getId());
        rmqMessage.setMessageBody(message.getMessageBody());

        String body = JSONObject.toJSONString(rmqMessage);
        org.apache.rocketmq.common.message.Message rmessage = //
                new org.apache.rocketmq.common.message.Message(message.getConsumerQueue(), body.getBytes(Charset.forName("UTF-8")));

        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new RmqException("send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RmqException("send message to rocketmq failed ", e);
        }

        log.info("【resendMessage】success, messageId={}", message.getId());
    }

    @Override
    public void resendMessageById(String messageId) {
        log.info("【resendMessageById】start, messageId={}", messageId);
        if (StringUtils.isBlank(messageId)) {
            throw new CheckException("messageId is empty");
        }

        // 校验消息是否存在
        Message message = mapper.selectByPrimaryKey(messageId);
        if (message == null) {
            throw new CheckException("message not exist");
        }

        // 增加重发次数
        mapper.addResendTimes(messageId);

        // 发送MQ消息
        RmqMessage rmqMessage = new RmqMessage();
        rmqMessage.setMessageId(message.getId());
        rmqMessage.setMessageBody(message.getMessageBody());
        String body = JSONObject.toJSONString(rmqMessage);
        org.apache.rocketmq.common.message.Message rmessage = //
                new org.apache.rocketmq.common.message.Message(message.getConsumerQueue(), body.getBytes(Charset.forName("UTF-8")));

        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                throw new RmqException("send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RmqException("send message to rocketmq failed ", e);
        }
        log.info("【resendMessageById】success, messageId={}", messageId);
    }

    @Override
    public int updateMessageDead(Short resendTimes) {
        return mapper.updateMessageDead(resendTimes);
    }
}
