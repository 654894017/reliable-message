package com.cn.rmq.schedule.tasks;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cn.rmq.api.schedule.service.IRecoverMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description:
 * 消息恢复子系统定时任务
 * </p>
 *
 * @author Chen Nan
 * @date 2019/3/18.
 */
@Component
@Slf4j
public class RecoverTask {

    @DubboReference
    private IRecoverMessageService recoverMessageService;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void task() {
        log.info("【RecoverTask】start");

        recoverMessageService.recoverSendingMessage();

        log.info("【RecoverTask】end");
    }
}
