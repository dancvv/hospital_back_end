package com.atguigu.yygh.order.service.impl;

import com.atguigu.yygh.enums.PaymentStatusEnum;
import com.atguigu.yygh.hosp.HospitalFeignClient;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.model.order.PaymentInfo;
import com.atguigu.yygh.order.mapper.PaymentMapper;
import com.atguigu.yygh.order.service.OrderService;
import com.atguigu.yygh.order.service.PaymentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, PaymentInfo> implements PaymentService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HospitalFeignClient hospitalFeignClient;
    //    向支付记录表添加信息
    @Override
    public void savePaymentInfo(OrderInfo order, Integer paymentType) {
//        根据订单id和支付类型，查询支付记录表是否存在相同订单
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", order.getId());
        wrapper.eq("payment_type", paymentType);
        Long count = baseMapper.selectCount(wrapper);
        if(count > 0){
            return;
        }
//        添加记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentStatus(paymentType);
        paymentInfo.setOutTradeNo(order.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(order.getReserveDate()).toString("yyyy-MM-dd")+"|"+order.getHosname()+"|"+order.getDepname()+"|"+order.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(order.getAmount());
        baseMapper.insert(paymentInfo);
    }
}
