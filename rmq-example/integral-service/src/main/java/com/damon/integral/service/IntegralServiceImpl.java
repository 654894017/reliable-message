package com.damon.integral.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damon.integral.entity.Integral;
import com.damon.integral.mapper.IntegralMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
@Service
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, Integral> implements IIntegralService {

    @Autowired
    private IntegralMapper integralMapper;

    @Override
    public boolean updateUserIntegral(Long userId, Long integral) {
        int result = integralMapper.updateUserIntegral(userId, integral);
        return result > 0;
    }

}
