package com.damon.integral.application;

import com.damon.integra.api.IIntegralApplicationService;
import com.damon.integral.service.IIntegralDomainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0")
public class IntegralApplicationService implements IIntegralApplicationService {

    @Autowired
    private IIntegralDomainService integralDomainService;

    @Override
    public boolean deductionIntegral(Long userId, Long integral, Long orderId) {
        return integralDomainService.deductionIntegral(userId, integral, orderId);
    }

    @Override
    public boolean rollbackDeductionIntegral(Long orderId) {
        return integralDomainService.rollbackDeductionIntegral(orderId);
    }
}
