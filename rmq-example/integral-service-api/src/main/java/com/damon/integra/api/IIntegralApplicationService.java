package com.damon.integra.api;

public interface IIntegralApplicationService {

    boolean deductionIntegral(Long userId, Long integral, Long orderId);

    boolean rollbackDeductionIntegral(Long orderId);

}
