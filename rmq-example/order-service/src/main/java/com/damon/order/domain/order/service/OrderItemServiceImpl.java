package com.damon.order.domain.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damon.order.domain.order.po.OrderItem;
import com.damon.order.domain.order.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luxianping
 * @since 2022-06-04
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {


}
