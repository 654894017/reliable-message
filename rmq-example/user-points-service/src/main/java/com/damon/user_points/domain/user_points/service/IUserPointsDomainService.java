package com.damon.user_points.domain.user_points.service;

public interface IUserPointsDomainService {

    boolean deductionIntegral(Long userId, Long points, Long orderId);

    boolean rollbackDeductionIntegral(Long orderId);
}
