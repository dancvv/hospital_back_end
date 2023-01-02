package com.atguigu.yygh.order.service;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

//    订单列表
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    boolean insertOne();

    //    根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    //    获取订单
    Map<String, Object> show(Long id);

//    取消订单
    Object cancelOrder(Long orderId);

//    就诊通知
    void patientTips();
}
