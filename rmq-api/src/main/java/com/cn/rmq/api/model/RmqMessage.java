package com.cn.rmq.api.model;

import java.io.Serializable;

import lombok.Data;


@Data
public class RmqMessage implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -723806634354652620L;
    private String messageId;
    private String messageBody;
}
