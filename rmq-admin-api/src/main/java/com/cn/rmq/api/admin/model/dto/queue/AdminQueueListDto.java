package com.cn.rmq.api.admin.model.dto.queue;

import org.apache.commons.lang3.StringUtils;

import com.cn.rmq.api.admin.model.dto.PageReq;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminQueueListDto extends PageReq {
    /**
     * 
     */
    private static final long serialVersionUID = 7109016150898123183L;

    private String id;

    private String businessName;

    private String consumerQueue;

    /**
     * 创建时间起
     */
    private String createStartTime;

    /**
     * 创建时间止
     */
    private String createEndTime;

    public String getCreateStartTime() {
        if (StringUtils.isBlank(createStartTime)) {
            return null;
        }
        return createStartTime;
    }

    public String getCreateEndTime() {
        if (StringUtils.isBlank(createEndTime)) {
            return null;
        }
        return createEndTime;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
