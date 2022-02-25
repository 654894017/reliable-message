package com.damon.rmq.schedule.tasks;

import com.damon.rmq.api.schedule.service.IRecoverMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 消息恢复子系统定时任务
 *
 * @author xianping_lu
 */
@Slf4j
@Component
public class RecoverTask {

    @DubboReference
    private IRecoverMessageService recoverMessageService;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void task() {

        log.info("recover task start");

        recoverMessageService.recoverSendingMessage();

        log.info("recover task end");
    }
}
