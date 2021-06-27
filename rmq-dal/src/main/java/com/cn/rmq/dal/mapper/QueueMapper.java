package com.cn.rmq.dal.mapper;

import java.util.List;

import com.cn.rmq.api.admin.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.admin.model.vo.queue.AdminQueueVo;
import com.cn.rmq.api.model.po.Queue;

/**
 * @author Chen Nan
 */
public interface QueueMapper extends BaseMapper<Queue, String> {

    /**
     * CMS获取消息列表
     *
     * @param req 请求参数
     * @return 消息列表
     */
    List<AdminQueueVo> cmsListPage(AdminQueueListDto req);
}