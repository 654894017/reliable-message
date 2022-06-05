package com.damon.inventory.application;

import com.damon.inventory.service.GoodsInventoryDomainService;
import com.damon.inventory.service.IGoodsInventoryDomainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0")
public class GoodsInventoryApplicationService implements IGoodsInventoryApplicationService{
    @Autowired
    private IGoodsInventoryDomainService goodsInventoryDomainService;

    @Override
    public boolean deductionGoodsInventory(GoodsInventoryDedcutionDTO dedcution) {
        return goodsInventoryDomainService.deductionGoodsInventory(dedcution);
    }

    @Override
    public boolean rollbackGoodsInventory(Long orderId) {
        return goodsInventoryDomainService.rollbackGoodsInventory(orderId);
    }
}
