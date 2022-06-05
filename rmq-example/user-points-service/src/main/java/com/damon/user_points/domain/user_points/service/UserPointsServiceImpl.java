package com.damon.user_points.domain.user_points.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damon.user_points.domain.user_points.entity.UserPoints;
import com.damon.user_points.domain.user_points.mapper.UserPointsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
@Service
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements IUserPointsService {

    @Autowired
    private UserPointsMapper userPointsMapper;

    @Override
    public boolean updateUserPoints(Long userId, Long integral) {
        int result = userPointsMapper.updateUserPoints(userId, integral);
        return result > 0;
    }

}
