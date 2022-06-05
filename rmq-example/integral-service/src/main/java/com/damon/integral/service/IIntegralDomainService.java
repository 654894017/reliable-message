package com.damon.integral.service;

public interface IIntegralDomainService {

    boolean deductionIntegral(Long userId, Long integral, Long orderId);

    boolean rollbackDeductionIntegral(Long orderId);
}
