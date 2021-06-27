package com.cn.rmq.api.admin.model.vo.message;

import com.cn.rmq.api.enums.AlreadyDeadEnum;
import com.cn.rmq.api.enums.MessageStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
public class AdminMessageVo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8321689092538900877L;

    private String id;

    private String consumerQueue;

    private Short resendTimes;

    private Byte alreadyDead;
    private String alreadyDeadName;

    private Byte status;
    private String statusName;

    private LocalDateTime createTime;

    private LocalDateTime confirmTime;

    private LocalDateTime updateTime;

    private String messageBody;

    public String getAlreadyDeadName() {
        return AlreadyDeadEnum.format(alreadyDead);
    }

    public String getStatusName() {
        return MessageStatusEnum.format(status);
    }
}
