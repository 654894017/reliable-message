package com.damon.rmq.api.admin.model.dto.system;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>资源记录</p>
 */
@Data
public class SysResourceDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6201761375538547626L;
    private String resourceId;
    private String name;
    private String url;
    private Byte type;
    private String icon;
    private Integer priority;
    private String parentId;
    private String parentName;
    private String permission;
    private Byte status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer page;
    private Integer rows;
}