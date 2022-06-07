package com.damon.inventory.domain.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import com.damon.inventory.domain.entity.GoodsInventoryDedcutionDO;
import com.damon.inventory.domain.po.GoodsInventory;
import com.damon.inventory.domain.po.GoodsInventoryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsInventoryDomainService implements IGoodsInventoryDomainService {
    private final static String COMMITTED = "committed";
    private final static String ROLLBACK = "rollback";
    @Autowired
    private IGoodsInventoryService goodsInventoryService;
    @Autowired
    private IGoodsInventoryLogService goodsInventoryLogService;

    @Transactional
    @Override
    public boolean deductionGoodsInventory(GoodsInventoryDedcutionDO dedcution) {
        GoodsInventoryLog log = new GoodsInventoryLog();
        log.setStatus(COMMITTED);
        log.setOrderId(dedcution.getOrderId());
        log.setPlacOrderGoodsListJson(JSONObject.toJSONString(dedcution.getGoods()));
        try {
            goodsInventoryLogService.save(log);
        } catch (DuplicateKeyException e) {
            String errorMessage = "订单：%s，库存已扣除，不允许重复扣除。";
            throw new RuntimeException(String.format(errorMessage, dedcution.getOrderId()), e);
        }
        for (OrderGoodsInventoryDedcutionDTO.PlaceOrderGoods goods : dedcution.getGoods()) {
            if (!goodsInventoryService.updateGoodsInventory(goods.getGoodsId(), goods.getNumber())) {
                throw new RuntimeException("商品：" + goods.getGoodsId() + "，库存不足");
            }
        }
        return Boolean.TRUE;
    }

    @Transactional
    @Override
    public boolean rollbackGoodsInventory(Long orderId) {
        LambdaQueryWrapper<GoodsInventoryLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsInventoryLog::getOrderId, orderId);
        GoodsInventoryLog goodsInventoryLog = goodsInventoryLogService.getOne(queryWrapper);
        //如果不存在日志记录，记录一个回滚日志，防止悬挂，业务幂等使用
        if (goodsInventoryLog == null) {
            GoodsInventoryLog log = new GoodsInventoryLog();
            log.setStatus(ROLLBACK);
            log.setOrderId(orderId);
            goodsInventoryLogService.save(log);
            return Boolean.TRUE;
        }

        if (ROLLBACK.equals(goodsInventoryLog.getStatus())) {
            return Boolean.TRUE;
        } else {
            goodsInventoryLog.setStatus(ROLLBACK);
            goodsInventoryLogService.updateById(goodsInventoryLog);
        }

        List<GoodsInventoryDedcutionDO.PlaceOrderGoods> goodsList = JSONArray.parseArray(goodsInventoryLog.getPlacOrderGoodsListJson(), GoodsInventoryDedcutionDO.PlaceOrderGoods.class);
        for (GoodsInventoryDedcutionDO.PlaceOrderGoods goods : goodsList) {
            LambdaQueryWrapper<GoodsInventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GoodsInventory::getId, goods.getGoodsId());
            GoodsInventory goodsInventory = goodsInventoryService.getOne(wrapper);
            goodsInventory.setNumber(goodsInventory.getNumber() + goods.getNumber());
            goodsInventoryService.updateById(goodsInventory);
        }
        return Boolean.TRUE;
    }

}
