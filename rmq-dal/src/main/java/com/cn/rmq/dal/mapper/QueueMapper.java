package com.cn.rmq.dal.mapper;

import java.util.List;

import com.cn.rmq.api.admin.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.admin.model.vo.queue.AdminQueueVo;
import com.cn.rmq.api.model.po.Queue;


public interface QueueMapper extends BaseMapper<Queue, String> {

    /**
     * 管理台获取消息列表
     * @param req
     * @return
     */
    List<AdminQueueVo> adminListPage(AdminQueueListDto req);
}