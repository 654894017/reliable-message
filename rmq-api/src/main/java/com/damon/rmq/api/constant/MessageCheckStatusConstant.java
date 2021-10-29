package com.damon.rmq.api.constant;
/**
 * 
 * 消息check返回状态码
 * 
 * 
 * @author xianping_lu
 *
 */
public interface MessageCheckStatusConstant {
    /**
     * 业务处理失败，RMQ删除半提交消息
     */
    public final static int FAILED_DELETE = 0;
    /**
     * 业务处理成功，RMQ发送半消息到MQ中间件
     */
    public final static int SUCCESS_NOFITY = 1;
    /**
     * 业务处理成功，RMQ删除半提交消息(用于不需要发送消息的场景)
     */
    public final static int SUCCESS_DELETE = 2;

}
