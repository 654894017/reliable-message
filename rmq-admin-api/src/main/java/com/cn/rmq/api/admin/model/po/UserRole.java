package com.cn.rmq.api.admin.model.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserRole implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1179984694633583773L;

    private String id;

    private String sysUserId;

    private String roleId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}