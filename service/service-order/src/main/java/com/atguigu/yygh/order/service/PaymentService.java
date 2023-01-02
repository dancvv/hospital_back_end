package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService {
//    向支付记录表添加信息
    void savePaymentInfo(OrderInfo order, Integer status);

//    更新订单信息表
    void paySuccess(String out_trade_no, Map<String, String> resultMap);
//    获取支付记录
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);
}
