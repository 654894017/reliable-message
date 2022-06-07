package com.damon.inventory.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damon.inventory.domain.po.GoodsInventory;
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
public interface GoodsInventoryMapper extends BaseMapper<GoodsInventory> {
    @Update("update goods_inventory set number = number - #{number} where id = #{goodsId} and number - #{number} >= 0")
    int updateGoodsInventory(@Param("goodsId") Long goodsId, @Param("number") Integer number);


}
