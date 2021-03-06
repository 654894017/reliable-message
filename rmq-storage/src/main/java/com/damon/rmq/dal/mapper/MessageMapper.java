package com.damon.rmq.dal.mapper;

import com.damon.rmq.api.model.dto.message.AdminMessageListQuery;
import com.damon.rmq.api.model.po.Message;
import com.damon.rmq.api.model.vo.AdminMessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MessageMapper extends BaseMapper<Message, String> {

    /**
     * 增加重发次数
     *
     * @param id 消息ID
     */
    void addResendTimes(String id);

    /**
     * 标记所有重发次数超过限制的消息为已死亡
     *
     * @param resendTimes 最大重发次数限制
     * @return 处理记录数量
     */
    int updateMessageDead(Short resendTimes);

    /**
     * CMS获取消息列表
     *
     * @param req 请求参数
     * @return 消息列表
     */
    List<AdminMessageVo> adminListPage(AdminMessageListQuery req);

    AdminMessageVo getMessage(@Param("queue") String queue, @Param("messageId") String messageId);

    int deleteMessage(@Param("queue") String queue, @Param("messageId") String messageId);

    int updateMessageStatus(@Param("queue") String queue, @Param("messageId") String messageId, @Param("status") int status);


}