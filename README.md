------------

## 介绍

**RMQ**（reliable-message-queue）是**基于可靠消息的最终一致性**的分布式事务解决方案。同时基于事务消息半提交原理，结合消息的回查机制，实现类似TCC的事务模型。


- RMQ不同于seata、tcc-transaction、Hmily等类似框架，需要在相同的协议下比如都是dubbo、spring cloud下才能够使用。RMQ给与用户最灵活的选择，不局限于dubbo、spring cloud，对方接口可以是grpc、thrift、php语言等类似接口。只要业务方接口提供类似Try、Commit、Cancel接口，或Commit、Cancel接口。我们在业务层面通过硬编码的形式实现类型TCC的效果。


## 框架定位
- RMQ本身不生产消息队列，只是消息的搬运工。
- RMQ框架提供消息预发送、消息发送、消息确认、消息恢复、消息管理等功能，结合成熟的消息中间件，解决分布式事务，达到数据最终一致性。
- RMQ基于事务消息半提交原理，结合消息的回查机制，实现类似TCC的事务模型（业务硬编码）。

------------

## Maven模块描述

| 模块名称 | 描述 |
| --- | --- |
| rmq-api | 提供业务系统调用的RMQ服务接口 |
| rmq-service-api | 基础消息服务接口、系统工具类、实体类封装 |
| rmq-service | RMQ服务接口实现、基础消息服务接口实现、消息管理子系统服务接口实现 |
| rmq-schedule-api | 消息确认子系统、消息恢复子系统服务接口 |
| rmq-schedule | 消息确认子系统，与上游业务系统确认消息是否发送<br>消息恢复子系统，重新发送消息给下游业务 |
| rmq-admin-api | 消息管理子系统服务接口、实体类封装 |
| rmq-admin | 消息管理子系统，提供消息管理后台 |
| rmq-dal | 数据库访问层： sql语句|

------------


## 在业务代码中引入RMQ的Dubbo服务
```
import org.apache.dubbo.config.annotation.DubboReference;
import com.cn.rmq.api.service.IReliableMessageService;

@DubboReference
private IReliableMessageService reliableMessageService;
```

## 编写消息发送方业务方法
结合事务消息实现TCC效果，如果不需要使用MQ传递领域消息到其他业务模块，可以在完成业务后删除事务消息，不需要confirm它。

```
public void doBusiness() {
        // 自定义消息队列名称
        String queue = "test_queue";
        // 消息内容, 如果传输对象，建议转换成json字符串
        String messageContent = "......";
        // 调用RMQ，预发送消息
        String messageId = reliableMessageService.createPreMessage(queue, messageContent);

        try{
         // 执行业务1(业务层面需要做好幂等、悬挂)
         // 执行业务2(业务层面需要做好幂等、悬挂)
         // 异步调用RMQ，确认发送消息(如果是当做分布式事务框架使用，不需要对外发送消息，则不需要进行消息confirm操作，直接调用deleteMessage删除事务消息即可)
         RpcContext.getContext().asyncCall(() -> reliableMessageService.confirmAndSendMessage(queue, messageId));
         
        }catch(Exception e){
         // 回滚业务1(业务层面需要做好幂等、悬挂、空回滚问题)
         // 回滚业务2(业务层面需要做好幂等、悬挂、空回滚问题)
         // 删除预发送消息
         RpcContext.getContext().asyncCall(() -> reliableMessageService.deleteMessage(queue, messageId));
         
        }
    }
```

## 编写业务回调check方法
当执行doBusiness异常回滚业务时，系统奔溃，消息确认子系统定时发起消息确认

```
@RequestMapping("check")
@ResponseBody
public CheckStatus checkBusStatus(BusReq req) {
   
   //如果业务执行成功 
   //return new CheckStats(0,1||2)
   
   
   //如果业务执行失败
   //回滚业务(业务层面需要做好幂等、悬挂、空回滚问题)
   //return new CheckStats(0,0)
   
}

CheckStatus 格式
{	
  "code": 0,  // 0 成功  1 失败 
  "data": 1   // 0 业务处理失败，删除半提交消息 1 业务处理成功，RMQ发送半消息到MQ中间件 2 业务处理成功，RMQ删除半提交消息 
}

```


## 编写消息消费方业务方法（RocketMQ）
```
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("xxxxxx_consumer_group");
consumer.setNamesrvAddr("localhost:9876");
consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
consumer.setConsumeThreadMax(1);
consumer.setConsumeThreadMin(1);
consumer.setMaxReconsumeTimes(1);
consumer.subscribe("xxxxxxx", "*");
consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            for (MessageExt message : msgs) {
                String body = new String(message.getBody(), Charset.forName("UTF-8"));
                //业务处理
                
                //删除事务消息
                reliableMessageService.deleteMessage(message.getTopic(), msg.getMessageId());
                log.info("xxxxxx-处理消息成功");
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Throwable e) {
            log.info("xxxxx-处理消息失败", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
};
consumer.start();
System.out.printf("xxxxx-consumer started.");
        
```

## 业务接口注意事项

- 幂等性：不管是MQ消费服务，还是业务提供的Try、Commit、Cancel接口都需要满足幂等性要求，因为极端异常情况下，消息确认子系统会check业务系统做数据一致性修正。存在重复调用的情况，也存在消息重复发送MQ的情况。
- 空回滚：业务系统有可能没执行Try，结果被执行Cancle的情况。需要保障不允许空回滚的情况。
- 悬挂：由于网络问题业务先被Cancel了然，后又收到Try的动作。需要保证Try不会被执行。

基于以上三个问题是分布式事务中一定会遇到的：可以引入一个**业务幂等表**来实现消息的幂等性、空回滚、悬挂问题。

------------

