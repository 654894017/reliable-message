package com.damon.user_points.domain.user_points.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
@Getter
@Setter
@TableName("user_points_log")
public class UserPointsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户积分
     */
    private Long orderId;

    private String status;

    private Long points;

    private Long placeOrderUserId;



}
