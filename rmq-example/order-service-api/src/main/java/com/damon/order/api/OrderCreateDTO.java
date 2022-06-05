package com.damon.order.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCreateDTO implements Serializable {
    private Long orderId;
    private Long userId;
    private List<OrderItemDTO> items;
    private Long deductionPoints;
    private Long givePoints;
    @Data
    public static class OrderItemDTO implements Serializable{
        private Long goodsId;
        private Integer number;
    }

}
