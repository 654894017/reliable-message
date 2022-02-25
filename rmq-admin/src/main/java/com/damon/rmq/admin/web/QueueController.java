package com.damon.rmq.admin.web;

import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.admin.model.po.SysUser;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.RspBase;
import com.damon.rmq.api.model.dto.queue.AdminQueueListQueryHelper;
import com.damon.rmq.api.model.dto.queue.QueueAddDto;
import com.damon.rmq.api.model.dto.queue.QueueUpdateDto;
import com.damon.rmq.api.model.vo.AdminQueueVo;
import com.damon.rmq.api.service.IQueueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>队列管理制器</p>
 */
@RestController
@RequestMapping(value = "queue")
@Slf4j
public class QueueController {

    @DubboReference
    private IQueueService queueService;

    @GetMapping("page")
    public DataGrid<AdminQueueVo> page(@ModelAttribute AdminQueueListQueryHelper query) {
        log.info("【queue-page】start：" + query);
        DataGrid<AdminQueueVo> rsp = queueService.querylistPage(query);
        log.info("【queue-page】success");
        return rsp;
    }

    @PostMapping
    public RspBase<Void> create(@ModelAttribute @Valid QueueAddDto queue, HttpSession session) {
        log.info("【queue-create】add：" + queue);
        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        queue.setCreateUser(sysUser.getUserName());
        queue.setUpdateUser(sysUser.getUserName());
        queueService.create(queue);
        log.info("【queue-create】add");
        return new RspBase<>();
    }

    @PutMapping
    public RspBase<Void> update(@ModelAttribute @Valid QueueUpdateDto req, HttpSession session) {
        log.info("【queue-update】create：" + req);
        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        req.setUpdateUser(sysUser.getUserName());
        queueService.update(req);
        log.info("【queue-update】create");
        return new RspBase<>();
    }

    @DeleteMapping("{id}")
    public RspBase<Void> delete(@PathVariable("id") String id) {
        log.info("【queue-delete】start：" + id);
        queueService.deleteByPrimaryKey(id);
        log.info("【queue-delete】success：" + id);
        return new RspBase<>();
    }

    @PostMapping("{id}/resend")
    public RspBase<Integer> resend(@PathVariable("id") String id) {
        log.info("【queue-resend】start：" + id);
        int count = queueService.resendDead(id);
        RspBase<Integer> rspBase = new RspBase<>();
        rspBase.data(count);
        log.info("【queue-resend】success:id={},count={}", id, count);
        return rspBase;
    }
}
