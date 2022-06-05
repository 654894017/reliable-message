package com.damon.order.domain.order.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDO implements Serializable {

    private Long userId;

    private Long id;

    private Integer status;

    private Long deductionPoints;

    private Long givePoints;

    private List<OrderItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItem implements Serializable {
        private Long goodsId;
        private Integer number;
        private Long orderId;
    }

}
