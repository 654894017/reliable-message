package com.damon.order.domain.order.service;

import com.damon.order.domain.order.entity.OrderDO;
import com.damon.order.domain.order.po.Order;

public interface IOrderDomainService {

    Long preCreateOrder(OrderDO order);

     boolean updateOrderStatusToSucceeded(Long orderId);

    boolean updateOrderStatusToFailed(Long orderId);

    Order get(Long orderId);
}
