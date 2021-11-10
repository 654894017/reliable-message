package com.damon.rmq.schedule.tasks;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.damon.rmq.api.schedule.service.ICheckMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息确认子系统定时任务
 * 
 * @author xianping_lu
 *
 */
@Component
@Slf4j
public class CheckTask {

    @DubboReference
    private ICheckMessageService checkMessageService;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void task() {
        log.info("check task start");

        checkMessageService.checkWaitingMessage();

        log.info("check task end");
    }
}