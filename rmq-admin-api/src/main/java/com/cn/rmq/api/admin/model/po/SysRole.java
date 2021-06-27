package com.cn.rmq.api.admin.model.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysRole implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6296013337469656578L;

    private String roleId;

    private String roleName;

    private Byte status;

    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;
}