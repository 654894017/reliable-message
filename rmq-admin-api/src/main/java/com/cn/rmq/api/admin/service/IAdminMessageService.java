package com.cn.rmq.api.admin.service;

import com.cn.rmq.api.admin.model.dto.DataGrid;
import com.cn.rmq.api.admin.model.dto.message.AdminMessageListDto;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.service.IBaseService;

/**
 * 消息服务接口
 *
 */
public interface IAdminMessageService extends IBaseService<Message, String> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 数据列表
     */
    DataGrid listPage(AdminMessageListDto req);

    /**
     * 重发某个消息队列中的全部已死亡的消息
     *
     * @param consumerQueue 消费队列
     * @return 重发的消息数量
     */
    int resendAllDeadMessageByQueueName(String consumerQueue);
}
