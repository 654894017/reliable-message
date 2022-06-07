package com.damon.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInventoryDedcutionDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private List<OrderGoodsInventoryDedcutionDTO.PlaceOrderGoods> goods;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceOrderGoods implements Serializable{
        private Long goodsId;
        private Integer number;
    }



}
