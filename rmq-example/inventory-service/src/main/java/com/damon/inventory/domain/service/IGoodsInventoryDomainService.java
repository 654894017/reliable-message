package com.damon.inventory.domain.service;

import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import com.damon.inventory.domain.entity.GoodsInventoryDedcutionDO;

public interface IGoodsInventoryDomainService {

    boolean deductionGoodsInventory(GoodsInventoryDedcutionDO goodsInventoryDedcutionDO);

    boolean rollbackGoodsInventory(Long orderId);
}
