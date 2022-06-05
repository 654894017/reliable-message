package com.damon.inventory.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damon.inventory.entity.GoodsInventory;
import com.damon.inventory.mapper.GoodsInventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
@Service
public class GoodsInventoryServiceImpl extends ServiceImpl<GoodsInventoryMapper, GoodsInventory> implements IGoodsInventoryService {

    @Autowired
    private GoodsInventoryMapper goodsInventoryMapper;

    @Override
    public boolean updateGoodsInventory(Long goodsId, Integer number) {
        int result = goodsInventoryMapper.updateGoodsInventory(goodsId, number);
        return result > 0;
    }

}
