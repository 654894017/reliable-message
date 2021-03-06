package com.damon.rmq.api.admin.model.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 179991613511070365L;

    private String sysUserId;

    private Integer areaId;

    private String userName;

    private Byte userStatus;

    private String userPhone;

    private String userEmail;

    private String userPwd;

    private Byte userType;

    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;
}