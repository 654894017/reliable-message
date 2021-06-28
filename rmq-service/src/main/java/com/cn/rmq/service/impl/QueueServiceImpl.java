package com.cn.rmq.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.cn.rmq.api.DataGrid;
import com.cn.rmq.api.exceptions.CheckException;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.model.dto.queue.QueueAddDto;
import com.cn.rmq.api.model.dto.queue.QueueUpdateDto;
import com.cn.rmq.api.model.po.Queue;
import com.cn.rmq.api.model.vo.AdminQueueVo;
import com.cn.rmq.api.service.IMessageService;
import com.cn.rmq.api.service.IQueueService;
import com.cn.rmq.dal.mapper.QueueMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.util.IdUtil;

/**
 * 队列服务实现
 * 
 * @author xianping_lu
 *
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class QueueServiceImpl extends BaseServiceImpl<QueueMapper, Queue, String> implements IQueueService {

    @DubboReference
    private IMessageService messageService;
    
    @Override
    public DataGrid listPage(AdminQueueListDto req) {
        Page<Object> pageInfo = PageHelper.startPage(req.getPage(), req.getRows());
        List<AdminQueueVo> list = mapper.adminListPage(req);

        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list);
        dataGrid.setTotal(pageInfo.getTotal());
        return dataGrid;
    }
    

    @Override
    public void add(QueueAddDto req) {
        Queue queue = new Queue();
        queue.setConsumerQueue(req.getConsumerQueue());
        int count = mapper.count(queue);
        if (count > 0) {
            throw new CheckException("consumerQueue:" + req.getConsumerQueue() + " is exist");
        }

        BeanUtils.copyProperties(req, queue);
        queue.setId(IdUtil.simpleUUID());
        queue.setCreateTime(LocalDateTime.now());
        queue.setUpdateTime(LocalDateTime.now());
        mapper.insertSelective(queue);
    }

    @Override
    public void update(QueueUpdateDto req) {
        Queue queue = mapper.selectByPrimaryKey(req.getId());
        if (queue == null) {
            throw new CheckException("queue not exist");
        }

        // 校验消费队列是否重复
        Queue checkCondition = new Queue();
        checkCondition.setConsumerQueue(req.getConsumerQueue());
        Queue check = mapper.get(checkCondition);
        if (check != null && !check.getId().equals(queue.getId())) {
            throw new CheckException("consumerQueue:" + req.getConsumerQueue() + " is exist");
        }

        BeanUtils.copyProperties(req, queue);
        queue.setUpdateTime(LocalDateTime.now());
        mapper.updateByPrimaryKeySelective(queue);
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
