package com.damon.rmq.api.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class TransactionMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -723806634354652620L;
    private String messageId;
    private String messageBody;
}
