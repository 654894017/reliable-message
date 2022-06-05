package com.damon.order.application;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.damon.integra.api.IIntegralApplicationService;
import com.damon.inventory.application.GoodsInventoryDedcutionDTO;
import com.damon.inventory.application.IGoodsInventoryApplicationService;
import com.damon.order.api.IOrderApplicationService;
import com.damon.order.api.OrderCreateDTO;
import com.damon.order.api.OrderMessageDTO;
import com.damon.order.application.assembler.OrderAssembler;
import com.damon.order.domain.order.aggregate.OrderDO;
import com.damon.order.domain.order.po.Order;
import com.damon.order.domain.order.po.OrderItem;
import com.damon.order.domain.order.service.IOrderDomainService;
import com.damon.order.domain.order.service.OrderDomainService;
import com.damon.rmq.api.service.IReliableMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0")
@Slf4j
public class OrderApplicationService implements IOrderApplicationService {
    @Autowired
    private IOrderDomainService orderDomainService;
    @DubboReference(version = "1.0",retries = -1,timeout = 500000)
    private IGoodsInventoryApplicationService inventoryApplicationService;
    @DubboReference(version = "1.0",retries = -1,timeout = 500000)
    private IIntegralApplicationService integralApplicationService;
    @DubboReference(retries = -1,timeout = 500000)
    private IReliableMessageService reliableMessageService;

    private final static String ORDER_QUEUE = "user_order_queue";

    @Override
    public Long placeOrder(OrderCreateDTO order){

        Long orderId = IdWorker.getId();
        order.setOrderId(orderId);
        OrderMessageDTO orderMessageDTO = OrderMessageDTO.builder().orderId(orderId).build();
        String messageId = reliableMessageService.createPreMessage(ORDER_QUEUE, JSONObject.toJSONString(orderMessageDTO));

        OrderDO orderDO = OrderAssembler.toDO(order);
        try{
            //1.预创建订单
            orderDomainService.preCreateOrder(orderDO);
            GoodsInventoryDedcutionDTO goodsInventoryDedcutionDTO = new GoodsInventoryDedcutionDTO();
            goodsInventoryDedcutionDTO.setOrderId(orderId);
            List<GoodsInventoryDedcutionDTO.PlaceOrderGoods> placeOrderGoodsList = order.getItems().stream().map(item->{
                GoodsInventoryDedcutionDTO.PlaceOrderGoods placeOrderGoods = new GoodsInventoryDedcutionDTO.PlaceOrderGoods();
                placeOrderGoods.setNumber(item.getNumber());
                placeOrderGoods.setGoodsId(item.getGoodsId());
                return placeOrderGoods;
            }).collect(Collectors.toList());
            goodsInventoryDedcutionDTO.setGoods(placeOrderGoodsList);
            //2.扣减库存
            inventoryApplicationService.deductionGoodsInventory(goodsInventoryDedcutionDTO);
            //3.扣减积分
            integralApplicationService.deductionIntegral(order.getUserId(), order.getDeductionPoints(), orderId);
            //4.更新订单状态创建成功
            orderDomainService.updateOrderStatusToSucceeded(orderId);
        }catch (Throwable e){
            log.error("用户:{},下单失败, 订单id：{}", order.getUserId(), order.getOrderId(),e);
            rollabck(orderId);
            // 失败回滚数据，并删除预发送的消息
            reliableMessageService.deleteMessage(ORDER_QUEUE, messageId);
            return orderId;
        }

        // 异步调用RMQ，确认发送消息(如果是当做分布式事务框架使用，不需要对外发送消息，则不需要进行消息confirm操作，直接调用deleteMessage删除事务消息即可)
        reliableMessageService.confirmAndSendMessage(ORDER_QUEUE, messageId);
        return orderId;
    }

    private void rollabck(Long orderId){
        //回滚库存
        inventoryApplicationService.rollbackGoodsInventory(orderId);
        //回滚积分
        integralApplicationService.rollbackDeductionIntegral(orderId);
        //更新订单状态创建失败
        orderDomainService.updateOrderStatusToFailed(orderId);
    }

    @Override
    public Integer callbackCheckOrderStatus(Long orderId){
       Order order = orderDomainService.get(orderId);
        if (order == null
                ||OrderDomainService.ORDER_PRE_CREATE_STATUS.equals(order.getStatus())
                || OrderDomainService.ORDER_CREATE_FAILED.equals(order.getStatus())
        ) {
            rollabck(orderId);
            return 0;
        }
        return 1;
    }

}
