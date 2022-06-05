package com.damon.order.domain.order.service;

import com.damon.order.domain.order.aggregate.OrderDO;
import com.damon.order.domain.order.po.Order;
import com.damon.order.domain.order.po.OrderItem;

import java.util.List;

public interface IOrderDomainService {

    Long preCreateOrder(OrderDO order);

     boolean updateOrderStatusToSucceeded(Long orderId);

    boolean updateOrderStatusToFailed(Long orderId);

    Order get(Long orderId);
}
