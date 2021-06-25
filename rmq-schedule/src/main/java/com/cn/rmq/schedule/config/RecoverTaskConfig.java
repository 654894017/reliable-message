package com.cn.rmq.schedule.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * <p>Title:</p>
 * <p>Description:
 * 消息恢复定时任务配置
 * </p>
 *
 * @author Chen Nan
 * @date 2019/3/18.
 */
@Component
@ConfigurationProperties(prefix = "schedule.recover")
@Data
public class RecoverTaskConfig {
    
    /**
     * 分页大小
     */
    private Integer pageSize =  1000;
    
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
    private Integer keepAliveTime = 5000;
    /**
     * 缓存队列大小
     */
    private Integer queueCapacity = Integer.MAX_VALUE;
    /**
     * 等待所有线程执行完成的超时时间（单位：毫秒）
     *
     */
    private Integer waitCompleteTimeout = 10000;

    /**
     * 消息重发时间间隔（单位：分钟）
     * 举例： [4, 10, 30, 60, 120, 360]
     * 消息确认会下下游业务发送首次消息，4分钟内，下游业务没有确认消费该消息，则消息恢复子系统会重发该消息。
     * 再过10分钟（也就是消息确认后14分钟内），下游业务没有确认消费该消息，则消息恢复子系统会重发该消息。
     * 以此类推
     */
    private List<Long> interval;
}
