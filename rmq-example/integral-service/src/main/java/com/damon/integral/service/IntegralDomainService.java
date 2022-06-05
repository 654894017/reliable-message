package com.damon.integral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.damon.integral.entity.Integral;
import com.damon.integral.entity.IntegralLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegralDomainService implements IIntegralDomainService {

    private final static String COMMITTED = "committed";
    private final static String ROLLBACK = "rollback";
    @Autowired
    private IIntegralService integralService;
    @Autowired
    private IIntegralLogService integralLogService;

    @Override
    @Transactional
    public boolean deductionIntegral(Long userId, Long integral, Long orderId) {

        IntegralLog log = new IntegralLog();
        log.setStatus(COMMITTED);
        log.setOrderId(orderId);
        log.setIntegral(integral);
        log.setPlaceOrderUserId(userId);
        try {
            integralLogService.save(log);
        } catch (DuplicateKeyException e) {
            String errorMessage = "用户：%s，订单：%s，积分已抵扣，不允许重复抵扣。";
            throw new RuntimeException(String.format(errorMessage, userId, orderId), e);
        }

        LambdaQueryWrapper<Integral> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Integral::getUserId, userId);
        Integral userIntegral = integralService.getOne(wrapper);
        if (userIntegral == null) {
            throw new RuntimeException("用户积分账户不存在");
        }

        if (integralService.updateUserIntegral(userId, integral)) {
            return Boolean.TRUE;
        }

        throw new RuntimeException("用户积分不足");
    }

    @Override
    @Transactional
    public boolean rollbackDeductionIntegral(Long orderId) {
        LambdaQueryWrapper<IntegralLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IntegralLog::getOrderId, orderId);
        IntegralLog userIntegralLog = integralLogService.getOne(queryWrapper);
        //如果不存在日志记录，记录一个回滚日志，防止悬挂，业务幂等使用
        if (userIntegralLog == null) {
            IntegralLog log = new IntegralLog();
            log.setStatus(ROLLBACK);
            log.setOrderId(orderId);
            log.setIntegral(0L);
            log.setPlaceOrderUserId(0L);
            integralLogService.save(log);
            return Boolean.TRUE;
        }

        if (ROLLBACK.equals(userIntegralLog.getStatus())) {
            return Boolean.TRUE;
        } else {
            userIntegralLog.setStatus(ROLLBACK);
            integralLogService.updateById(userIntegralLog);
        }

        //回滚已被抵扣的积分到用户账号
        LambdaQueryWrapper<Integral> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Integral::getUserId, userIntegralLog.getPlaceOrderUserId());
        Integral userIntegral = integralService.getOne(wrapper);
        userIntegral.setIntegral(userIntegral.getIntegral() + userIntegralLog.getIntegral());
        integralService.updateById(userIntegral);
        return Boolean.TRUE;
    }
}
