package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;

public interface PaymentService {
//    向支付记录表添加信息
    void savePaymentInfo(OrderInfo order, Integer status);
}
