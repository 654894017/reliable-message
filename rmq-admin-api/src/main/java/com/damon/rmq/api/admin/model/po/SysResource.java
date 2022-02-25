package com.damon.rmq.api.admin.model.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysResource implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -198744923348263967L;

    private String resourceId;

    private String name;

    private String url;

    private Byte type;

    private String icon;

    private Integer priority;

    private String parentId;

    private String permission;

    private Byte status;

    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;

}