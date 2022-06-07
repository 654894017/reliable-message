package com.damon.inventory.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.damon.inventory.domain.po.GoodsInventory;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
public interface IGoodsInventoryService extends IService<GoodsInventory> {

    boolean updateGoodsInventory(Long userId, Integer number);

}
