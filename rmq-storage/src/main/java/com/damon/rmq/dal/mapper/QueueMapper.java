package com.damon.rmq.dal.mapper;

import com.damon.rmq.api.model.dto.queue.AdminQueueListQueryHelper;
import com.damon.rmq.api.model.po.Queue;
import com.damon.rmq.api.model.vo.AdminQueueVo;

import java.util.List;


public interface QueueMapper extends BaseMapper<Queue, String> {

    /**
     * 管理台获取消息列表
     *
     * @param req
     * @return
     */
    List<AdminQueueVo> adminListPage(AdminQueueListQueryHelper req);
}