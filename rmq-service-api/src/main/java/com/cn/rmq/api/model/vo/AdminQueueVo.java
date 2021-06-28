package com.cn.rmq.api.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class AdminQueueVo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2980819442660824776L;

    private String id;

    private String businessName;

    private String consumerQueue;

    private String checkUrl;

    private Integer checkDuration;

    private Short checkTimeout;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createUser;

    private String updateUser;
}
