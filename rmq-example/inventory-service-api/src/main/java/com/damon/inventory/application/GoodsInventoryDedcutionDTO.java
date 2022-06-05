package com.damon.inventory.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInventoryDedcutionDTO implements Serializable {

    private Long orderId;

    private List<PlaceOrderGoods> goods;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceOrderGoods implements Serializable{
        private Long goodsId;
        private Integer number;
    }

}
