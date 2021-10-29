package com.damon.rmq.api.schedule.model.dto;

import com.damon.rmq.api.model.po.Message;

import cn.hutool.json.JSONUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class ScheduleMessageDto extends Message {
    /**
     * 
     */
    private static final long serialVersionUID = -2783490154261815300L;
    /**
     * 创建时间止
     */
    private String createEndTime;
    /**
     * 确认时间止
     */
    private String confirmEndTime;

    /**
     * 分页页码
     */
    private Integer pageNum;
    /**
     * 分页数量
     */
    private Integer pageSize;
    /**
     * 是否需要计算总数
     */
    private Boolean count;
    /**
     * 排序
     */
    private String orderBy;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
