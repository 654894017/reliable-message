package com.damon.order.api;

public interface IOrderApplicationService {
    Long placeOrder(OrderCreateDTO order);
    Integer callbackCheckOrderStatus(Long orderId);
}
