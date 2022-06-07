package com.damon.order.application.assembler;

import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import com.damon.order.api.OrderCreateDTO;
import com.damon.order.domain.order.entity.OrderDO;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAssembler {

    public static OrderDO toDO(OrderCreateDTO dto){
        OrderDO orderDO =  new OrderDO();
        orderDO.setId(dto.getOrderId());
        orderDO.setDeductionPoints(dto.getDeductionPoints());
        //orderDO.setGivePoints(dto.getGivePoints());
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


    public static OrderGoodsInventoryDedcutionDTO toGoodsInventoryDedcutionDTO(OrderCreateDTO order) {
        OrderGoodsInventoryDedcutionDTO goodsInventoryDedcutionDTO = new OrderGoodsInventoryDedcutionDTO();
        goodsInventoryDedcutionDTO.setOrderId(order.getOrderId());
        List<OrderGoodsInventoryDedcutionDTO.PlaceOrderGoods> placeOrderGoodsList = order.getItems().stream().map(item->{
            OrderGoodsInventoryDedcutionDTO.PlaceOrderGoods placeOrderGoods = new OrderGoodsInventoryDedcutionDTO.PlaceOrderGoods();
            placeOrderGoods.setNumber(item.getNumber());
            placeOrderGoods.setGoodsId(item.getGoodsId());
            return placeOrderGoods;
        }).collect(Collectors.toList());
        goodsInventoryDedcutionDTO.setGoods(placeOrderGoodsList);
        return goodsInventoryDedcutionDTO;
    }


}
