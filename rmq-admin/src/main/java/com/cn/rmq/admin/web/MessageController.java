package com.cn.rmq.admin.web;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.rmq.api.admin.model.dto.DataGrid;
import com.cn.rmq.api.admin.model.dto.message.AdminMessageListDto;
import com.cn.rmq.api.admin.model.vo.message.AdminMessageVo;
import com.cn.rmq.api.admin.service.IAdminMessageService;
import com.cn.rmq.api.model.Constants;
import com.cn.rmq.api.model.dto.RspBase;
import com.cn.rmq.api.model.po.Message;
import com.cn.rmq.api.service.IMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>消息管理制器</p>
 *
 */
@RestController
@RequestMapping(value = "/message")
@Slf4j
public class MessageController {

    @DubboReference
    private IAdminMessageService cmsMessageService;
    @DubboReference
    private IMessageService messageService;

    @GetMapping("/page")
    public Object page(@ModelAttribute AdminMessageListDto req) {
        log.info("【message-page】start：" + req);
        DataGrid rsp = cmsMessageService.listPage(req);
        log.info("【message-page】success");
        return rsp;
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable("id") String id) {
        log.info("【message-get】start：" + id);
        Message message = cmsMessageService.selectByPrimaryKey(id);
        RspBase rspBase = new RspBase();
        if (message != null) {
            AdminMessageVo messageVo = new AdminMessageVo();
            BeanUtils.copyProperties(message, messageVo);
            rspBase.setData(messageVo);
            log.info("【message-get】success：" + id);
        } else {
            log.info("【message-get】fail, not exist：" + id);
            rspBase.code(Constants.CODE_FAILURE).msg("message not exist");
        }
        return rspBase;
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        log.info("【message-delete】start：" + id);
        cmsMessageService.deleteByPrimaryKey(id);
        RspBase rspBase = new RspBase();
        log.info("【message-delete】success：" + id);
        return rspBase;
    }

    @PostMapping("/{id}/resend")
    public Object resend(@PathVariable("id") String id) {
        log.info("【message-resend】start：" + id);
        messageService.resendMessageById(id);
        RspBase rspBase = new RspBase();
        log.info("【message-resend】success:" + id);
        return rspBase;
    }
}
