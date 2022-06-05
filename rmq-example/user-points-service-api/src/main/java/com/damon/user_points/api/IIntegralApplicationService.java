package com.damon.user_points.api;

public interface IIntegralApplicationService {

    boolean deductionIntegral(Long userId, Long points, Long orderId);

    boolean rollbackDeductionIntegral(Long orderId);

}
