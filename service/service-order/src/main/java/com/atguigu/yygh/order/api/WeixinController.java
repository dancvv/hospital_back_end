package com.atguigu.yygh.order.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.order.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/order/weixin")
@RestController
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @GetMapping("createNative/{orderId}")
    public Result<Object> createNative(@PathVariable Long orderId){
        Map map = weixinService.createNative(orderId);
        return Result.ok(map);
    }
}
