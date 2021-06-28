package com.cn.rmq.api.service;

import com.cn.rmq.api.DataGrid;
import com.cn.rmq.api.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.model.dto.queue.QueueAddDto;
import com.cn.rmq.api.model.dto.queue.QueueUpdateDto;
import com.cn.rmq.api.model.po.Queue;

/**
 * 消费队列服务接口
 *
 * @author Chen Nan
 */
public interface IQueueService extends IBaseService<Queue, String> {

    /**
     * 添加
     * @param req 添加对象参数
     */
    void add(QueueAddDto req);

    /**
     * 更新
     * @param req 更新对象参数
     */
    void update(QueueUpdateDto req);

    /**
     * 重发队列死亡消息
     * @param id 队列ID
     * @return 重发消息数量
     */
    int resendDead(String id);
    

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 数据列表
     */
    DataGrid listPage(AdminQueueListDto req);
}
