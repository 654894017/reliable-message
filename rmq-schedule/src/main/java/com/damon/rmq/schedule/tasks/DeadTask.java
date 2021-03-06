package com.damon.rmq.schedule.tasks;

import com.damon.rmq.api.service.IMessageService;
import com.damon.rmq.schedule.config.RecoverTaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 标记消息死亡定时任务
 *
 * @author xianping_lu
 */
@Slf4j
@Component
public class DeadTask {

    @DubboReference
    private IMessageService messageService;

    @Autowired
    private RecoverTaskConfig config;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void task() {

        log.info("dead task start");

        short maxResendTimes = (short) config.getInterval().size();

        int updateCount = messageService.updateMessageDead(maxResendTimes);

        log.info("dead task maxResendTimes={}, updateCount={}", maxResendTimes, updateCount);

        log.info("dead task end");
    }
}
