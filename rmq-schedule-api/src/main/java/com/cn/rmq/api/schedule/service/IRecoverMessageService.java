package com.cn.rmq.api.schedule.service;

/**
 *  消息恢复子系统服务接口
 *  
 * @author xianping_lu
 *
 */
public interface IRecoverMessageService {
    /**
     * 处理状态为“发送中”但长时间没有被成功确认消费的消息
     */
    void recoverSendingMessage();
}
