package com.damon.rmq.api.model.dto.message;

import cn.hutool.json.JSONUtil;
import com.damon.rmq.api.PageReq;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
public class AdminMessageListQuery extends PageReq {
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
