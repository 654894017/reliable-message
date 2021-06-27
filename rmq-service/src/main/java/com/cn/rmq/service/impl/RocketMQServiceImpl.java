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

import com.alibaba.fastjson.JSONObject;
import com.cn.rmq.api.enums.MessageStatusEnum;
import com.cn.rmq.api.exceptions.CheckException;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.RmqMessage;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.service.IRmqService;
import com.cn.rmq.dal.mapper.MessageMapper;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * RocketMQ消息服务实现
 * 
 * @author xianping_lu
 *
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
@Slf4j
public class RocketMQServiceImpl extends BaseServiceImpl<MessageMapper, Message, String> implements IRmqService {

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

        return id;
    }

    @Override
    public void confirmAndSendMessage(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            throw new CheckException("messageId is empty");
        }

        // 获取消息
        Message message = mapper.selectByPrimaryKey(messageId);
        if (message == null) {
            throw new CheckException("message not exist");
        }

        // 更新消息状态为发送中
        Message updateBean = new Message();
        updateBean.setId(messageId);
        updateBean.setUpdateTime(LocalDateTime.now());
        updateBean.setConfirmTime(LocalDateTime.now());
        // 发送MQ消息
        RmqMessage rmqMessage = new RmqMessage();
        rmqMessage.setMessageId(messageId);
        rmqMessage.setMessageBody(message.getMessageBody());

        String body = JSONObject.toJSONString(rmqMessage);
        org.apache.rocketmq.common.message.Message rmessage = //
                new org.apache.rocketmq.common.message.Message(message.getConsumerQueue(), body.getBytes(Charset.forName("UTF-8")));
        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
                updateBean.setStatus(MessageStatusEnum.SENDING.getValue());
            } else {
                log.error("send message to rocketmq failed, rocketmq return status code: {}", result.getSendStatus());
                updateBean.setStatus(MessageStatusEnum.SEND_FAILED.getValue());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            log.error("send message to rocketmq failed ", e);
            updateBean.setStatus(MessageStatusEnum.SEND_FAILED.getValue());
        }
        mapper.updateByPrimaryKeySelective(updateBean);
    }

    @Override
    public void deleteMessageById(String messageId) {
        mapper.deleteByPrimaryKey(messageId);
    }
}
