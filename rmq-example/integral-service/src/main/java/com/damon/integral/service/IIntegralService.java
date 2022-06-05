package com.damon.integral.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.damon.integral.entity.Integral;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
public interface IIntegralService extends IService<Integral> {
    boolean updateUserIntegral(Long userId, Long integral);
}
