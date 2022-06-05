package com.damon.inventory.application;

public interface IGoodsInventoryApplicationService {

    boolean deductionGoodsInventory(GoodsInventoryDedcutionDTO dedcution);

    boolean rollbackGoodsInventory(Long orderId);
}
