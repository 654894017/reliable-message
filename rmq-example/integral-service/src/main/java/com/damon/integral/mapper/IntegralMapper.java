package com.damon.integral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damon.integral.entity.Integral;
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
public interface IntegralMapper extends BaseMapper<Integral> {
    @Update("update integral set integral = integral - #{integral} where user_id = #{userId} and integral - #{integral} > 0")
    int updateUserIntegral(@Param("userId") Long userId, @Param("integral") Long integral);

}
