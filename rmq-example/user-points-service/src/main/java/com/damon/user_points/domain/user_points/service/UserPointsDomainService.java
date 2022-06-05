package com.damon.user_points.domain.user_points.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.damon.user_points.domain.user_points.entity.UserPoints;
import com.damon.user_points.domain.user_points.entity.UserPointsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPointsDomainService implements IUserPointsDomainService {

    private final static String COMMITTED = "committed";
    private final static String ROLLBACK = "rollback";
    @Autowired
    private IUserPointsService userPointsService;
    @Autowired
    private IUserPointsLogService userPointsLogService;

    @Override
    @Transactional
    public boolean deductionIntegral(Long userId, Long points, Long orderId) {

        UserPointsLog log = new UserPointsLog();
        log.setStatus(COMMITTED);
        log.setOrderId(orderId);
        log.setPoints(points);
        log.setPlaceOrderUserId(userId);
        try {
            userPointsLogService.save(log);
        } catch (DuplicateKeyException e) {
            String errorMessage = "用户：%s，订单：%s，积分已抵扣，不允许重复抵扣。";
            throw new RuntimeException(String.format(errorMessage, userId, orderId), e);
        }

        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getUserId, userId);
        UserPoints userIntegral = userPointsService.getOne(wrapper);
        if (userIntegral == null) {
            throw new RuntimeException("用户积分账户不存在");
        }

        if (userPointsService.updateUserPoints(userId, points)) {
            return Boolean.TRUE;
        }

        throw new RuntimeException("用户积分不足");
    }

    @Override
    @Transactional
    public boolean rollbackDeductionIntegral(Long orderId) {
        LambdaQueryWrapper<UserPointsLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPointsLog::getOrderId, orderId);
        UserPointsLog userIntegralLog = userPointsLogService.getOne(queryWrapper);
        //如果不存在日志记录，记录一个回滚日志，防止悬挂，业务幂等使用
        if (userIntegralLog == null) {
            UserPointsLog log = new UserPointsLog();
            log.setStatus(ROLLBACK);
            log.setOrderId(orderId);
            log.setPoints(0L);
            log.setPlaceOrderUserId(0L);
            userPointsLogService.save(log);
            return Boolean.TRUE;
        }

        if (ROLLBACK.equals(userIntegralLog.getStatus())) {
            return Boolean.TRUE;
        } else {
            userIntegralLog.setStatus(ROLLBACK);
            userPointsLogService.updateById(userIntegralLog);
        }

        //回滚已被抵扣的积分到用户账号
        LambdaQueryWrapper<UserPoints> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPoints::getUserId, userIntegralLog.getPlaceOrderUserId());
        UserPoints userPoints = userPointsService.getOne(wrapper);
        userPoints.setPoints(userPoints.getPoints() + userIntegralLog.getPoints());
        userPointsService.updateById(userPoints);
        return Boolean.TRUE;
    }
}
