package com.damon.order.domain.order.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.damon.order.domain.order.entity.OrderDO;
import com.damon.order.domain.order.po.Order;
import com.damon.order.domain.order.po.OrderItem;
import com.damon.order.infrastructure.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDomainService implements IOrderDomainService {

    public final static Integer ORDER_PRE_CREATE_STATUS = 2;

    public final static Integer ORDER_CREATE_SUCCEEDED = 4;

    public final static Integer ORDER_CREATE_FAILED = 6;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderItemService orderItemService;

    @Transactional
    @Override
    public Long preCreateOrder(OrderDO orderDO) {
        Order order = BeanMapper.map(orderDO, Order.class);
        List<OrderItem> orderItems = BeanMapper.mapList(orderDO.getItems(), OrderDO.OrderItem.class, OrderItem.class);
        order.setStatus(ORDER_PRE_CREATE_STATUS);
        orderService.save(order);
        orderItemService.saveBatch(orderItems);
        return order.getId();
    }

    @Override
    public boolean updateOrderStatusToSucceeded(Long orderId) {
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Order::getStatus, ORDER_CREATE_SUCCEEDED).eq(Order::getId, orderId);
        orderService.update(updateWrapper);
        return Boolean.TRUE;
    }


    @Override
    public boolean updateOrderStatusToFailed(Long orderId) {
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Order::getStatus, ORDER_CREATE_FAILED).eq(Order::getId, orderId);
        orderService.update(updateWrapper);
        return Boolean.TRUE;
    }

    @Override
    public Order get(Long orderId) {

     return orderService.getById(orderId);
    }

}
