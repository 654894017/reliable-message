package com.cn.rmq.api.constant;

public interface MessageCheckStatusConstant {
    /**
     * 业务处理失败，需要RMQ重新发起重试
     */
    public final static int FAILED = 0;
    /**
     * 业务处理成功，RMQ发送半消息到MQ中间件
     */
    public final static int SUCCESS_NOFITY = 1;
    /**
     * 业务处理成功，RMQ删除半提交消息
     */
    public final static int SUCCESS_DELETE = 2;

}
