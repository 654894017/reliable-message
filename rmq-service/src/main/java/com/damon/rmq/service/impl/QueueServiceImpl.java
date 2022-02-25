package com.damon.rmq.service.impl;

import cn.hutool.core.util.IdUtil;
import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.exceptions.CheckException;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.queue.AdminQueueListQueryHelper;
import com.damon.rmq.api.model.dto.queue.QueueAddDto;
import com.damon.rmq.api.model.dto.queue.QueueUpdateDto;
import com.damon.rmq.api.model.po.Queue;
import com.damon.rmq.api.model.vo.AdminQueueVo;
import com.damon.rmq.api.service.IMessageService;
import com.damon.rmq.api.service.IQueueService;
import com.damon.rmq.dal.mapper.QueueMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 队列服务实现
 *
 * @author xianping_lu
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class QueueServiceImpl extends BaseServiceImpl<QueueMapper, Queue, String> implements IQueueService {

    @DubboReference
    private IMessageService messageService;

    @Override
    public DataGrid<AdminQueueVo> querylistPage(AdminQueueListQueryHelper query) {

        Page<AdminQueueVo> pageInfo = PageHelper.startPage(query.getPage(), query.getRows());
        List<AdminQueueVo> list = mapper.adminListPage(query);

        DataGrid<AdminQueueVo> dataGrid = new DataGrid<>();
        dataGrid.setRows(list);
        dataGrid.setTotal(pageInfo.getTotal());
        return dataGrid;
    }

    @Override
    public void create(QueueAddDto req) {
        Queue queue = new Queue();
        queue.setConsumerQueue(req.getConsumerQueue());
        BeanUtils.copyProperties(req, queue);
        queue.setId(IdUtil.simpleUUID());
        queue.setCreateTime(LocalDateTime.now());
        queue.setUpdateTime(LocalDateTime.now());
        try {
            mapper.insertSelective(queue);
        } catch (DuplicateKeyException e) {
            throw new CheckException("consumerQueue:" + req.getConsumerQueue() + " is exist");
        }
    }

    @Override
    public void update(QueueUpdateDto req) {

        Queue queue = mapper.selectByPrimaryKey(req.getId());
        if (queue == null) {
            throw new CheckException("queue not exist");
        }

        BeanUtils.copyProperties(req, queue);
        queue.setUpdateTime(LocalDateTime.now());
        try {
            mapper.updateByPrimaryKeySelective(queue);
        } catch (DuplicateKeyException e) {
            throw new CheckException("consumerQueue:" + req.getConsumerQueue() + " is exist");
        }
    }

    @Override
    public int resendDead(String id) {
        Queue queue = mapper.selectByPrimaryKey(id);
        if (queue == null) {
            throw new CheckException("queue not exist");
        }
        return messageService.resendAllDeadMessageByQueueName(queue.getConsumerQueue());
    }
}
