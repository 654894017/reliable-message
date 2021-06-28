------------

#### 介绍

**RMQ**（reliable-message-queue）是**基于可靠消息的最终一致性**的分布式事务解决方案。


## 框架定位
- RMQ本身不生产消息队列，只是消息的搬运工。
- RMQ框架提供消息预发送、消息发送、消息确认、消息恢复、消息管理等功能，结合成熟的消息中间件，解决分布式事务，达到数据最终一致性。

------------

#### Maven模块描述

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


#### 在业务代码中引入RMQ的Dubbo服务
```
import org.apache.dubbo.config.annotation.Reference;
import com.cn.rmq.api.service.IRmqService;

@Reference
private IRmqService rmqService;
```

#### 编写消息发送方业务方法
```
public void doBusiness() {
        // 自定义消息队列名称
        String queue = "test_queue";
        // 消息内容, 如果传输对象，建议转换成json字符串
        String messageContent = "......";

        // 调用RMQ，预发送消息
        String messageId = rmqService.createPreMessage(queue, messageContent);

        // 执行业务
        ...
        ...

        // 异步调用RMQ，确认发送消息
        RpcContext.getContext().asyncCall(() -> rmqService.confirmAndSendMessage(messageId));
    }
```

#### 编写消息消费方业务方法
```
public void handleMsg(RmqMessage msg) {
        try {
            String messageContent = msg.getMessageBody();

            // 执行业务
            ...
            ...

            // 通知RMQ消息消费成功
            // 如果使用的是RMQ的directSendMessage，则无需通知
            if (StringUtils.isNotBlank(msg.getMessageId())) {
                rmqService.deleteMessageById(msg.getMessageId());
            }
        } catch (Exception e) {
            ...
        }
    }
```

------------
