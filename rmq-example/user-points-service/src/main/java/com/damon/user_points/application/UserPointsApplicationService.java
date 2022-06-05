package com.damon.user_points.application;

import com.damon.user_points.api.IIntegralApplicationService;
import com.damon.user_points.domain.user_points.service.IUserPointsDomainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0")
public class UserPointsApplicationService implements IIntegralApplicationService {

    @Autowired
    private IUserPointsDomainService integralDomainService;

    @Override
    public boolean deductionIntegral(Long userId, Long points, Long orderId) {
        return integralDomainService.deductionIntegral(userId, points, orderId);
    }

    @Override
    public boolean rollbackDeductionIntegral(Long orderId) {
        return integralDomainService.rollbackDeductionIntegral(orderId);
    }
}
