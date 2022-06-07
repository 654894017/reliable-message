package com.damon.inventory.application;

public interface IGoodsInventoryApplicationService {

    boolean deductionGoodsInventory(OrderGoodsInventoryDedcutionDTO dedcution);

    boolean rollbackGoodsInventory(Long orderId);
}
