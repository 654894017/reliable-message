package com.damon.order.controller;

import com.damon.order.api.IOrderApplicationService;
import com.damon.order.api.OrderMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@RequestMapping
@Controller
public class OrderStatusCheceController {

    @Autowired
    private IOrderApplicationService orderApplicationService;

    @PostMapping("order/status/check")
    @ResponseBody
    public Map<String, Object> checkOrderStatus(@RequestBody OrderMessageDTO message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("data", orderApplicationService.callbackCheckOrderStatus(message.getOrderId()));
        return result;
    }


}
