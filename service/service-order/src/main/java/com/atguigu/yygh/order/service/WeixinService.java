package com.atguigu.yygh.order.service;

import java.util.Map;

public interface WeixinService {
    Map createNative(Long orderId);

    //        调用微信接口实现支付状态查询
    Map<String, String> queryPayStatus(Long orderId);
//    退款
    Boolean refund(Long orderId);
}
