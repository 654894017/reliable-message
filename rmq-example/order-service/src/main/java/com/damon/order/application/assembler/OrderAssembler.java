package com.damon.order.application.assembler;

import com.damon.order.api.OrderCreateDTO;
import com.damon.order.domain.order.aggregate.OrderDO;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAssembler {

    public static OrderDO toDO(OrderCreateDTO dto){
        OrderDO orderDO =  new OrderDO();
        orderDO.setId(dto.getOrderId());
        orderDO.setDeductionPoints(dto.getDeductionPoints());
        orderDO.setGivePoints(dto.getGivePoints());
        orderDO.setUserId(dto.getUserId());

        List<OrderDO.OrderItem> orderItems = dto.getItems().stream().map(item->
            OrderDO.OrderItem.builder().goodsId(item.getGoodsId()).orderId(dto.getOrderId()).number(item.getNumber()).build()
        ).collect(Collectors.toList());
        orderDO.setItems(orderItems);
        return orderDO;
    }

    public static OrderCreateDTO toDTO(OrderDO order){
        return null;
    }


}
