package com.cn.rmq.admin.web;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.rmq.api.DataGrid;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.dto.RspBase;
import com.cn.rmq.api.model.dto.message.AdminMessageListQuery;
import com.cn.rmq.api.model.vo.AdminMessageVo;
import com.cn.rmq.api.service.IMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息管理制器
 * 
 * @author xianping_lu
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "message")
public class MessageController {
    @DubboReference
    private IMessageService messageService;

    @GetMapping("page")
    public Object page(@ModelAttribute AdminMessageListQuery query) {
        log.info("【message-page】start：" + query);
        DataGrid rsp = messageService.listPage(query);
        log.info("【message-page】success");
        return rsp;
    }

    @GetMapping("{queue}/{id}")
    public Object get(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("get queue: {}, message id : {} ", queue, id);
        RspBase rspBase = new RspBase();
        AdminMessageVo message = messageService.get(queue, id);
        if (message != null) {
            rspBase.setData(message);
            log.info("get queue: {}, message id : {} , succeed ", queue, id);
        } else {
            log.error("get queue: {}, message id : {} , not exist ", queue, id);
            rspBase.code(Constants.CODE_FAILURE).msg("message not exist");
        }
        return rspBase;
    }

    @DeleteMapping("{queue}/{id}")
    public Object delete(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("【message-delete】start：" + id);
        messageService.delete(queue, id);
        RspBase rspBase = new RspBase();
        log.info("【message-delete】success：" + id);
        return rspBase;
    }

    @PostMapping("{queue}/{id}/resend")
    public Object resend(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("【message-resend】start：" + id);
        messageService.resendMessage(queue,id);
        RspBase rspBase = new RspBase();
        log.info("【message-resend】success:" + id);
        return rspBase;
    }
}
