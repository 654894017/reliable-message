package com.damon.rmq.api.service;

/**
 * 消息服务接口
 */
public interface IReliableMessageService {
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
    void confirmAndSendMessage(String queue, String messageId);

    /**
     * 删除消息
     *
     * @param queue
     * @param messageId
     */
    void deleteMessage(String queue, String messageId);

    /**
     * 直接发送消息
     *
     * @param consumerQueue
     * @param messageId
     * @param messageBody
     */
    void directSendMessage(String consumerQueue, String messageId, String messageBody);
}
