package com.damon.inventory.application.assembler;

import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import com.damon.inventory.domain.entity.GoodsInventoryDedcutionDO;
import com.damon.inventory.infrastructure.utils.BeanMapper;

public class GoodsInventoryAssembler {

    public static GoodsInventoryDedcutionDO toDO(OrderGoodsInventoryDedcutionDTO dto){
        return BeanMapper.map(dto, GoodsInventoryDedcutionDO.class);
    }


}
