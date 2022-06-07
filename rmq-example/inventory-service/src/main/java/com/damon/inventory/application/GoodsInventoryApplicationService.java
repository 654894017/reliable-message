package com.damon.inventory.application;


import com.damon.inventory.application.assembler.GoodsInventoryAssembler;
import com.damon.inventory.domain.entity.GoodsInventoryDedcutionDO;
import com.damon.inventory.domain.service.IGoodsInventoryDomainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0")
public class GoodsInventoryApplicationService implements IGoodsInventoryApplicationService {
    @Autowired
    private IGoodsInventoryDomainService goodsInventoryDomainService;

    @Override
    public boolean deductionGoodsInventory(OrderGoodsInventoryDedcutionDTO dedcution) {
        GoodsInventoryDedcutionDO goodsInventoryDedcutionDO = GoodsInventoryAssembler.toDO(dedcution);
        return goodsInventoryDomainService.deductionGoodsInventory(goodsInventoryDedcutionDO);
    }

    @Override
    public boolean rollbackGoodsInventory(Long orderId) {
        return goodsInventoryDomainService.rollbackGoodsInventory(orderId);
    }
}
