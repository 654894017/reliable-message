package com.damon.rmq.api.service;

import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.model.dto.message.AdminMessageListQuery;
import com.damon.rmq.api.model.po.Message;
import com.damon.rmq.api.model.vo.AdminMessageVo;

/**
 * 消息服务接口
 *
 * @author xianping_lu
 */
public interface IMessageService extends IBaseService<Message, String> {

    /**
     * 重发消息
     *
     * @param message 消息
     */
    void resendMessage(Message message);

    /**
     * 重发消息
     *
     * @param messageId 消息ID
     */
    void resendMessage(String queue, String messageId);

    /**
     * 标记所有重发次数超过限制的消息为已死亡
     *
     * @param resendTimes 最大重发次数限制
     * @return 处理记录数量
     */
    int updateMessageDead(Short resendTimes);

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 数据列表
     */
    DataGrid<AdminMessageVo> listPage(AdminMessageListQuery req);

    /**
     * 重发某个消息队列中的全部已死亡的消息
     *
     * @param consumerQueue 消费队列
     * @return 重发的消息数量
     */
    int resendAllDeadMessageByQueueName(String consumerQueue);

    AdminMessageVo get(String queue, String messageId);

    int delete(String queue, String messageId);
}
