package com.atguigu.yygh.order.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result<Object> submitOrder(@PathVariable String scheduleId,
                                      @PathVariable Long patientId){
        Long id = orderService.saveOrder(scheduleId, patientId);
        return Result.ok(id);
    }
}
