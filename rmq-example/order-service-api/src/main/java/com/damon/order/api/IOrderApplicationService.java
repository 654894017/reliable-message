package com.damon.order.api;

public interface IOrderApplicationService {
    /**
     * 商品下单
     * @param order
     * @return
     */
    Long placeOrder(OrderCreateDTO order);

    /**
     * 订单状态check
     * @param orderId
     * @return
     */
    Integer callbackCheckOrderStatus(Long orderId);
}
