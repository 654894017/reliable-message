package com.cn.rmq.schedule.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程配置
 * 
 * @author xianping_lu
 *
 */
@Configuration
public class ThreadPoolConfig {
    @Autowired
    private CheckTaskConfig checkTaskConfig;
    @Autowired
    private RecoverTaskConfig recoverTaskConfig;

    public static void main(String[] args) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("check-pool-").build();
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(5,
                100,
                5000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024),
                namedThreadFactory);
        
        for(int i=0;i<500;i++) {
            executor.submit(()->{
                System.out.println(1);
            });
        }
        
        while(true) {
            System.out.println(executor.getActiveCount());
            System.out.println(executor.getQueue().size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

       
        
    }
    
    @Bean
    public ThreadPoolExecutor checkExecutor() {
        // 为线程池起名
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("check-pool-").build();
        return new ThreadPoolExecutor(checkTaskConfig.getCorePoolSize(),
                checkTaskConfig.getMaxPoolSize(),
                checkTaskConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(checkTaskConfig.getQueueCapacity()),
                namedThreadFactory);
    }

    @Bean
    public ThreadPoolExecutor recoverExecutor() {
        // 为线程池起名
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("recover-pool-").build();
        return new ThreadPoolExecutor(recoverTaskConfig.getCorePoolSize(),
                recoverTaskConfig.getMaxPoolSize(),
                recoverTaskConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(recoverTaskConfig.getQueueCapacity()),
                namedThreadFactory);
    }
}
