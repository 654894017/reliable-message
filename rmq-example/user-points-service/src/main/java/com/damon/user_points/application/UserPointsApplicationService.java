package com.damon.user_points.application;

import com.damon.user_points.api.IUserPointsApplicationService;
import com.damon.user_points.domain.user_points.service.IUserPointsDomainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0")
public class UserPointsApplicationService implements IUserPointsApplicationService {

    @Autowired
    private IUserPointsDomainService integralDomainService;

    @Override
    public boolean deductionUserPoints(Long userId, Long points, Long orderId) {
        return integralDomainService.deductionUserPoints(userId, points, orderId);
    }

    @Override
    public boolean rollbackDeductionUserPoints(Long orderId) {
        return integralDomainService.rollbackDeductionUserPoints(orderId);
    }
}
