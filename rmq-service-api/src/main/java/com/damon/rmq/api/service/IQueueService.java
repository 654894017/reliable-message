package com.damon.rmq.api.service;

import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.model.dto.queue.AdminQueueListQueryHelper;
import com.damon.rmq.api.model.dto.queue.QueueAddDto;
import com.damon.rmq.api.model.dto.queue.QueueUpdateDto;
import com.damon.rmq.api.model.po.Queue;
import com.damon.rmq.api.model.vo.AdminQueueVo;

/**
 * 消费队列服务接口
 *
 * @author xianping_lu
 */
public interface IQueueService extends IBaseService<Queue, String> {

    /**
     * 添加
     *
     * @param req 添加对象参数
     */
    void create(QueueAddDto req);

    /**
     * 更新
     *
     * @param req 更新对象参数
     */
    void update(QueueUpdateDto req);

    /**
     * 重发队列死亡消息
     *
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
    DataGrid<AdminQueueVo> querylistPage(AdminQueueListQueryHelper query);
}
