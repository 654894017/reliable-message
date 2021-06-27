package com.cn.rmq.api.schedule.service;

/**
 * 消息确认子系统服务接口
 * 
 * @author xianping_lu
 *
 */
public interface ICheckMessageService {
    /**
     * 处理所有长时间未确认的消息，和上游业务系统确认是否发送该消息
     */
    void checkWaitingMessage();
}
