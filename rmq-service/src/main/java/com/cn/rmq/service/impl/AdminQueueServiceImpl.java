package com.cn.rmq.service.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;

import com.cn.rmq.api.admin.model.dto.DataGrid;
import com.cn.rmq.api.admin.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.admin.model.vo.queue.AdminQueueVo;
import com.cn.rmq.api.admin.service.IAdminQueueService;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.po.Queue;
import com.cn.rmq.dal.mapper.QueueMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 队列服务实现
 *
 * @author Chen Nan
 * @date 2019/3/11.
 */
@DubboService(timeout = Constants.SERVICE_TIMEOUT)
public class AdminQueueServiceImpl extends BaseServiceImpl<QueueMapper, Queue, String> implements IAdminQueueService {

    @Override
    public DataGrid listPage(AdminQueueListDto req) {
        Page<Object> pageInfo = PageHelper.startPage(req.getPage(), req.getRows());
        List<AdminQueueVo> list = mapper.cmsListPage(req);

        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list);
        dataGrid.setTotal(pageInfo.getTotal());
        return dataGrid;
    }
}
