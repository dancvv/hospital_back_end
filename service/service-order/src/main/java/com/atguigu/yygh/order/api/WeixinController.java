package com.atguigu.yygh.order.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.order.service.PaymentService;
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
    @Autowired
    private PaymentService paymentService;

    @GetMapping("createNative/{orderId}")
    public Result<Object> createNative(@PathVariable Long orderId){
        Map map = weixinService.createNative(orderId);
        return Result.ok(map);
    }
//    查询支付状态
    @GetMapping("queryPayStatus/{orderId}")
    public Result<Object> queryPayStatus(@PathVariable Long orderId){
//        调用微信接口实现支付状态查询
        Map<String, String> resultMap = weixinService.queryPayStatus(orderId);
//        是否支付成功
        if(resultMap == null){
            return Result.fail().message("支付出错");
        }
        if("SUCCESS".equals(resultMap.get("trade_state"))){
//            支付成功
            String our_trade_no = resultMap.get("out_trade_no");//订单编码
//            存入订单信息表中
            paymentService.paySuccess(our_trade_no, resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }
}
