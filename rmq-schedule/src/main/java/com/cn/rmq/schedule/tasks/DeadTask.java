package com.cn.rmq.schedule.tasks;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cn.rmq.api.service.IMessageService;
import com.cn.rmq.schedule.config.RecoverTaskConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 标记消息死亡定时任务
 * 
 * @author xianping_lu
 *
 */
@Component
@Slf4j
public class DeadTask {

    @DubboReference
    private IMessageService messageService;

    @Autowired
    private RecoverTaskConfig config;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void task() {
     
        log.info("【DeadTask】start");
        
        short maxResendTimes = (short) config.getInterval().size();
        
        int updateCount = messageService.updateMessageDead(maxResendTimes);
        
        log.info("【DeadTask】maxResendTimes={}, updateCount={}", maxResendTimes, updateCount);
        
        log.info("【DeadTask】end");
    }
}
