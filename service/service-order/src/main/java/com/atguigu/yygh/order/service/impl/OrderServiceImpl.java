package com.atguigu.yygh.order.service.impl;

import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.mapper.OrderMapper;
import com.atguigu.yygh.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        return null;
    }
}
