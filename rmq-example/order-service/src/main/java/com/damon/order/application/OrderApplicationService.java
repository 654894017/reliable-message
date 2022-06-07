package com.damon.order.application;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.damon.inventory.application.IGoodsInventoryApplicationService;
import com.damon.inventory.application.OrderGoodsInventoryDedcutionDTO;
import com.damon.order.api.IOrderApplicationService;
import com.damon.order.api.OrderCreateDTO;
import com.damon.order.api.OrderMessageDTO;
import com.damon.order.application.assembler.OrderAssembler;
import com.damon.order.domain.order.entity.OrderDO;
import com.damon.order.domain.order.po.Order;
import com.damon.order.domain.order.service.IOrderDomainService;
import com.damon.order.domain.order.service.OrderDomainService;
import com.damon.rmq.api.service.IReliableMessageService;
import com.damon.user_points.api.IUserPointsApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@DubboService(version = "1.0")
@Slf4j
public class OrderApplicationService implements IOrderApplicationService {

    private final static String ORDER_QUEUE = "user_order_queue";

    private final ThreadPoolExecutor ORDER_CREATE_THREAD_POOL = new ThreadPoolExecutor(200, 200,
            100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2048),
            new NamedThreadFactory("order_create_thread_pool_", true),
            new ThreadPoolExecutor.AbortPolicy());
    @Autowired
    private IOrderDomainService orderDomainService;
    @DubboReference(version = "1.0", retries = -1, timeout = 5000)
    private IGoodsInventoryApplicationService inventoryApplicationService;
    @DubboReference(version = "1.0", retries = -1, timeout = 5000)
    private IUserPointsApplicationService userPointsApplicationService;
    @DubboReference(retries = -1, timeout = 5000)
    private IReliableMessageService reliableMessageService;

    @Override
    public Long placeOrder(OrderCreateDTO order) {
        Long orderId = IdWorker.getId();
        order.setOrderId(orderId);
        OrderMessageDTO orderMessageDTO = OrderMessageDTO.builder().orderId(orderId).build();
        //发送前置消息
        String messageId = reliableMessageService.createPreMessage(ORDER_QUEUE, JSONObject.toJSONString(orderMessageDTO));
        OrderDO orderDO = OrderAssembler.toDO(order);
        OrderGoodsInventoryDedcutionDTO goodsInventoryDedcutionDTO = OrderAssembler.toGoodsInventoryDedcutionDTO(order);
        try {
            CompletableFuture.runAsync(() -> {
                //1.预创建订单
                orderDomainService.preCreateOrder(orderDO);
            }, ORDER_CREATE_THREAD_POOL).thenComposeAsync(__ -> {
                CompletableFuture<Void> inventoryFuture = CompletableFuture.runAsync(() -> {
                    //2-1.扣减库存
                    inventoryApplicationService.deductionGoodsInventory(goodsInventoryDedcutionDTO);
                }, ORDER_CREATE_THREAD_POOL);
                CompletableFuture<Void> pointsFuture = CompletableFuture.runAsync(() -> {
                    //2-2.扣减积分
                    userPointsApplicationService.deductionUserPoints(order.getUserId(), order.getDeductionPoints(), orderId);
                }, ORDER_CREATE_THREAD_POOL);
                return CompletableFuture.allOf(inventoryFuture, pointsFuture);
            }, ORDER_CREATE_THREAD_POOL).thenAcceptAsync(__ -> {
                //3.更新订单状态创建成功
                orderDomainService.updateOrderStatusToSucceeded(orderId);
            }, ORDER_CREATE_THREAD_POOL).join();

            CompletableFuture.runAsync(() -> {
                //4.异步调用RMQ，确认发送消息(如果是当做分布式事务框架使用，不需要对外发送消息，则不需要进行消息confirm操作，直接调用deleteMessage删除事务消息即可)
                reliableMessageService.confirmAndSendMessage(ORDER_QUEUE, messageId);
            }, ORDER_CREATE_THREAD_POOL);
            return orderId;
        } catch (Throwable e) {
            log.error("用户:{},下单失败, 订单id：{}", order.getUserId(), order.getOrderId(), e);
            return CompletableFuture.supplyAsync(() -> {
                rollabck(orderId);
                // 失败回滚数据，并删除预发送的消息
                reliableMessageService.deleteMessage(ORDER_QUEUE, messageId);
                return orderId;
            }, ORDER_CREATE_THREAD_POOL).join();
        }
    }

    /**
     * 回滚下单资源
     * @param orderId
     */
    private void rollabck(Long orderId) {
        //回滚库存
        inventoryApplicationService.rollbackGoodsInventory(orderId);
        //回滚积分
        userPointsApplicationService.rollbackDeductionUserPoints(orderId);
        //更新订单状态创建失败
        orderDomainService.updateOrderStatusToFailed(orderId);
    }

    @Override
    public Integer callbackCheckOrderStatus(Long orderId) {
        Order order = orderDomainService.get(orderId);
        if (order == null || OrderDomainService.ORDER_PRE_CREATE_STATUS.equals(order.getStatus()) || OrderDomainService.ORDER_CREATE_FAILED.equals(order.getStatus())) {
            rollabck(orderId);
            return 0;
        }
        return 1;
    }

}
