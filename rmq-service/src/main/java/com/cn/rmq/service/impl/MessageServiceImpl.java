package com.cn.rmq.service.impl;

import java.nio.charset.Charset;
import java.util.List;

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
import com.cn.rmq.api.DataGrid;
import com.cn.rmq.api.enums.AlreadyDeadEnum;
import com.cn.rmq.api.exceptions.CheckException;
import com.cn.rmq.api.exceptions.RmqException;
import com.cn.rmq.api.model.TransactionMessage;
import com.cn.rmq.api.model.dto.message.AdminMessageListQuery;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.model.vo.AdminMessageVo;
import com.cn.rmq.api.service.IMessageService;
import com.cn.rmq.dal.mapper.MessageMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 队列服务实现
 * 
 * @author xianping_lu
 *
 */
@DubboService
@Slf4j
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message, String> implements IMessageService {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public AdminMessageVo get(String queue, String messageId) {
        return mapper.getMessage(queue, messageId);
    }

    @Override
    public int delete(String queue, String messageId) {
        return mapper.deleteMessage(queue, messageId);
    }

    @Override
    public DataGrid listPage(AdminMessageListQuery req) {
        Page<Object> pageInfo = PageHelper.startPage(req.getPage(), req.getRows());
        List<AdminMessageVo> list = mapper.adminListPage(req);

        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list);
        dataGrid.setTotal(pageInfo.getTotal());
        return dataGrid;
    }

    @Override
    public int resendAllDeadMessageByQueueName(String consumerQueue) {
        log.info("【resendDead】start, consumerQueue={}", consumerQueue);
        if (StringUtils.isBlank(consumerQueue)) {
            throw new CheckException("consumerQueue is empty");
        }

        // 构造查询条件
        Message condition = new Message();
        condition.setConsumerQueue(consumerQueue);
        condition.setAlreadyDead(AlreadyDeadEnum.YES.getValue());

        int pageSize = 100;
        // 计数标识，首页需要获取消息总数
        boolean countFlag = true;
        int totalPage = 0;
        int totalCount = 0;

        for (int pageNum = 1;; pageNum++) {
            // 分页查询死亡消息
            Page<Message> pageInfo = PageHelper.startPage(pageNum, pageSize, countFlag);
            List<Message> list = mapper.list(condition);

            // 遍历消息列表，重发消息
            list.forEach((message) -> resendMessage(message));

            // 计数
            totalCount += list.size();

            if (countFlag) {
                countFlag = false;
                totalPage = pageInfo.getPages();
            }
            if (pageNum >= totalPage) {
                break;
            }
        }
        log.info("【resendDead】success, consumerQueue={}, totalCount={}", consumerQueue, totalCount);
        return totalCount;
    }

    @Override
    public void resendMessage(Message message) {
        log.info("【resendMessage】start, messageId={}", message.getId());
        // 增加重发次数
        mapper.addResendTimes(message.getId());

        // 发送MQ消息
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setMessageId(message.getId());
        transactionMessage.setMessageBody(message.getMessageBody());

        String body = JSONObject.toJSONString(transactionMessage);
        org.apache.rocketmq.common.message.Message rmessage = new org.apache.rocketmq.common.message.Message(
            message.getConsumerQueue(), 
            body.getBytes(Charset.forName("UTF-8"))
        );

        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("send message to rocketmq failed, rocketmq return status code: {}.", result.getSendStatus());
                throw new RmqException(
                    "send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            log.error("send message to rocketmq failed ", e);
            throw new RmqException("send message to rocketmq failed ", e);
        }

        log.info("【resendMessage】success, messageId={}", message.getId());
    }

    @Override
    public void resendMessage(String queue, String messageId) {
        log.info("【resendMessageById】start, messageId={}", messageId);
        if (StringUtils.isBlank(messageId)) {
            throw new CheckException("messageId is empty");
        }

        // 校验消息是否存在
        AdminMessageVo message = get(queue, messageId);
        if (message == null) {
            throw new CheckException("message not exist");
        }

        // 增加重发次数
        mapper.addResendTimes(messageId);

        // 发送MQ消息
        TransactionMessage rmqMessage = new TransactionMessage();
        rmqMessage.setMessageId(message.getId());
        rmqMessage.setMessageBody(message.getMessageBody());
        String body = JSONObject.toJSONString(rmqMessage);
        org.apache.rocketmq.common.message.Message rmessage = new org.apache.rocketmq.common.message.Message(
            message.getConsumerQueue(), body.getBytes(Charset.forName("UTF-8")));

        try {
            SendResult result = defaultMQProducer.send(rmessage);
            if (!result.getSendStatus().equals(SendStatus.SEND_OK)) {
                log.error("send message to rocketmq failed, rocketmq return status code: {}.z ",
                    result.getSendStatus());
                throw new RmqException(
                    "send message to rocketmq failed, rocketmq return status code: " + result.getSendStatus());
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            log.error("send message to rocketmq failed ", e);
            throw new RmqException("send message to rocketmq failed ", e);
        }
        log.info("【resendMessageById】success, messageId={}", messageId);
    }

    @Override
    public int updateMessageDead(Short resendTimes) {
        return mapper.updateMessageDead(resendTimes);
    }
}
