package com.damon.inventory.domain.service;

import com.damon.inventory.domain.entity.GoodsInventoryDedcutionDO;
import com.damon.inventory.domain.po.GoodsInventory;
import com.damon.inventory.infrastructure.utils.BeanMapper;

public class GoodsInventoryFactory {

    public static GoodsInventoryDedcutionDO getGoodsInventory(GoodsInventory po){
        return BeanMapper.map(po, GoodsInventoryDedcutionDO.class);
    }

    public static GoodsInventory createGoodsInventory(GoodsInventoryDedcutionDO goodsInventoryDO){
        return BeanMapper.map(goodsInventoryDO, GoodsInventory.class);
    }

}
