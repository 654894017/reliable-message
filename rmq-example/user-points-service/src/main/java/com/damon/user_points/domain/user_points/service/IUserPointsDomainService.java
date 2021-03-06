package com.damon.user_points.domain.user_points.service;

public interface IUserPointsDomainService {

    boolean deductionUserPoints(Long userId, Long points, Long orderId);

    boolean rollbackDeductionUserPoints(Long orderId);
}
