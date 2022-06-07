package com.damon.user_points.api;

public interface IUserPointsApplicationService {

    boolean deductionUserPoints(Long userId, Long points, Long orderId);

    boolean rollbackDeductionUserPoints(Long orderId);

}
