package com.damon.user_points.domain.user_points.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damon.user_points.domain.user_points.entity.UserPoints;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
public interface UserPointsMapper extends BaseMapper<UserPoints> {
    @Update("update user_points set points = points - #{points} where user_id = #{userId} and points - #{points} >= 0")
    int updateUserPoints(@Param("userId") Long userId, @Param("points") Long points);

}
