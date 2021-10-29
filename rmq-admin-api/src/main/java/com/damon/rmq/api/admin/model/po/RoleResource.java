package com.damon.rmq.api.admin.model.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RoleResource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5456732454234377097L;

    private String roleResourceId;

    private String roleId;

    private String resourceId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}