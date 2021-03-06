package com.damon.rmq.schedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息确认定时任务配置
 *
 * @author xianping_lu
 */
@Component
@ConfigurationProperties(prefix = "schedule.check")
@Data
public class CheckTaskConfig {
    /**
     * 分页大小
     */
    private Integer pageSize = 500;
    /**
     * 线程池最小线程数
     */
    private Integer corePoolSize = 10;
    /**
     * 线程池最大线程数
     */
    private Integer maxPoolSize = 100;
    /**
     * 线程运行的空闲时间
     */
    private Integer keepAliveTime = 60000;
    /**
     * 缓存队列大小
     */
    private Integer queueCapacity = 5000;
    /**
     * 等待所有线程执行完成的超时时间（单位：毫秒），
     * 因为确认超时时间最长为5秒，因此此处超时时间建议设置大于5秒，则足够所有线程完成。
     */
    private Integer waitCompleteTimeout = 10000;
}
