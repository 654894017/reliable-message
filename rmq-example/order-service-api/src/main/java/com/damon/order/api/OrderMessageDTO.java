package com.damon.order.api;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderMessageDTO implements Serializable {
    private Long orderId;
}
