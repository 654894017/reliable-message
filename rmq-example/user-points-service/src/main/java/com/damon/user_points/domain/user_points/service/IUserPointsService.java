package com.damon.user_points.domain.user_points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.damon.user_points.domain.user_points.entity.UserPoints;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
public interface IUserPointsService extends IService<UserPoints> {
    boolean updateUserPoints(Long userId, Long integral);
}
