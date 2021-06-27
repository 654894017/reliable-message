package com.cn.rmq.api.admin.service;

import com.cn.rmq.api.admin.model.dto.DataGrid;
import com.cn.rmq.api.admin.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.model.po.Queue;
import com.cn.rmq.api.service.IBaseService;

/**
 * 消费队列服务接口
 *
 */
public interface IAdminQueueService extends IBaseService<Queue, String> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 数据列表
     */
    DataGrid listPage(AdminQueueListDto req);
}
