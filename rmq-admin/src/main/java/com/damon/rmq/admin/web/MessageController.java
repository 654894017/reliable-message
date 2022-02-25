package com.damon.rmq.admin.web;

import com.damon.rmq.api.DataGrid;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.RspBase;
import com.damon.rmq.api.model.dto.message.AdminMessageListQuery;
import com.damon.rmq.api.model.vo.AdminMessageVo;
import com.damon.rmq.api.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 消息管理制器
 *
 * @author xianping_lu
 */
@Slf4j
@RestController
@RequestMapping(value = "message")
public class MessageController {
    @DubboReference
    private IMessageService messageService;

    @GetMapping("page")
    public DataGrid<AdminMessageVo> page(@ModelAttribute AdminMessageListQuery query) {
        log.info("【message-page】start：" + query);
        DataGrid<AdminMessageVo> rsp = messageService.listPage(query);
        log.info("【message-page】success");
        return rsp;
    }

    @GetMapping("{queue}/{id}")
    public RspBase<AdminMessageVo> get(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("get queue: {}, message id : {} ", queue, id);
        RspBase<AdminMessageVo> rspBase = new RspBase<>();
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
    public RspBase<Void> delete(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("【message-delete】start：" + id);
        messageService.delete(queue, id);
        RspBase<Void> rspBase = new RspBase<>();
        log.info("【message-delete】success：" + id);
        return rspBase;
    }

    @PostMapping("{queue}/{id}/resend")
    public RspBase<Void> resend(@PathVariable("queue") String queue, @PathVariable("id") String id) {
        log.info("【message-resend】start：" + id);
        messageService.resendMessage(queue, id);
        RspBase<Void> rspBase = new RspBase<>();
        log.info("【message-resend】success:" + id);
        return rspBase;
    }
}
