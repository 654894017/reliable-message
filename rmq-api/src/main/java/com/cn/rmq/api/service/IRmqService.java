package com.cn.rmq.api.service;

/**
 * 消息服务接口
 *
 */
public interface IRmqService {
    /**
     * 创建预发送消息
     *
     * @param consumerQueue 消费队列
     * @param messageBody   消息内容
     * @return 消息ID
     */
    String createPreMessage(String consumerQueue, String messageBody);

    /**
     * 确认发送消息
     *
     * @param messageId 消息 ID
     */
    void confirmAndSendMessage(String messageId);

    /**
     * 根据消息ID删除消息
     * @param messageId 消息ID
     */
    void deleteMessageById(String messageId);
}
