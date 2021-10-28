package com.cn.rmq.admin.web;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.rmq.api.DataGrid;
import com.cn.rmq.api.admin.model.po.SysUser;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.dto.RspBase;
import com.cn.rmq.api.model.dto.queue.AdminQueueListDto;
import com.cn.rmq.api.model.dto.queue.QueueAddDto;
import com.cn.rmq.api.model.dto.queue.QueueUpdateDto;
import com.cn.rmq.api.service.IQueueService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>队列管理制器</p>
 *
 */
@RestController
@RequestMapping(value = "queue")
@Slf4j
public class QueueController {

    @DubboReference(retries = -1)
    private IQueueService queueService;

    @GetMapping("page")
    public Object page(@ModelAttribute AdminQueueListDto req) {
        log.info("【queue-page】start：" + req);
        DataGrid rsp = queueService.listPage(req);
        log.info("【queue-page】success");
        return rsp;
    }

    @PostMapping
    public Object add(@ModelAttribute @Valid QueueAddDto req, HttpSession session) {
        log.info("【queue-create】add：" + req);
        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        req.setCreateUser(sysUser.getUserName());
        req.setUpdateUser(sysUser.getUserName());
        queueService.add(req);
        log.info("【queue-create】add");
        return new RspBase();
    }

    @PutMapping
    public Object update(@ModelAttribute @Valid QueueUpdateDto req, HttpSession session) {
        log.info("【queue-update】create：" + req);
        SysUser sysUser = (SysUser) session.getAttribute(Constants.SESSION_USER);
        req.setUpdateUser(sysUser.getUserName());
        queueService.update(req);
        log.info("【queue-update】create");
        return new RspBase();
    }

    @DeleteMapping("{id}")
    public Object delete(@PathVariable("id") String id) {
        log.info("【queue-delete】start：" + id);
        queueService.deleteByPrimaryKey(id);
        log.info("【queue-delete】success：" + id);
        return new RspBase();
    }

    @PostMapping("{id}/resend")
    public Object resend(@PathVariable("id") String id) {
        log.info("【queue-resend】start：" + id);
        int count = queueService.resendDead(id);
        RspBase rspBase = new RspBase();
        rspBase.data(count);
        log.info("【queue-resend】success:id={},count={}", id, count);
        return rspBase;
    }
}
