package com.atguigu.yygh.order.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderService;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    获取分页列表
    @GetMapping("{page}/{limit}")
    public Result<Object> index(@PathVariable Long page,
                                @PathVariable Long limit,
                                OrderQueryVo orderQueryVo){
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }
//    获取订单状态
    @GetMapping("getStatusList")
    public Result<Object> getStatusList(){
        return Result.ok(OrderStatusEnum.getStatusList());
    }
//    获取订单
    @GetMapping("/show/{id}")
    public Result<Object> get(@PathVariable Long id){
        return Result.ok(orderService.show(id));
    }

}
