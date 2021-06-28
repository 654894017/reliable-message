package com.cn.rmq.api.model.dto.message;

import org.apache.commons.lang3.StringUtils;

import com.cn.rmq.api.PageReq;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminMessageListDto extends PageReq {
    /**
     * 
     */
    private static final long serialVersionUID = -718052040845077423L;

    private String id;

    private String consumerQueue;

    private Byte alreadyDead;

    private Byte status;

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
