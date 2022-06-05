package com.damon.inventory.service;

import com.damon.inventory.application.GoodsInventoryDedcutionDTO;

public interface IGoodsInventoryDomainService {

    boolean deductionGoodsInventory(GoodsInventoryDedcutionDTO dedcution);

    boolean rollbackGoodsInventory(Long orderId);
}
