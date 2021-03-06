package com.damon.rmq.api.admin.model.dto.system;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>系统角色记录</p>
 *
 * @author Chen Nan
 */
@Data
public class SysRoleDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7971812678255316706L;

    private String roleId;

    private String roleName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Byte status;
    private Integer page;
    private Integer rows;
}