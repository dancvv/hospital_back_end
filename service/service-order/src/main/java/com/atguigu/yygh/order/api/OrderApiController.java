package com.atguigu.yygh.order.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.enums.OrderStatusEnum;
import com.atguigu.yygh.model.order.OrderInfo;
import com.atguigu.yygh.order.service.OrderService;
import com.atguigu.yygh.vo.order.OrderQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

//    创建订单
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result<Object> submitOrder(@PathVariable String scheduleId,
                                      @PathVariable Long patientId){
        Long id = orderService.saveOrder(scheduleId, patientId);
        return Result.ok(id);
    }

//    订单列表（条件查询带分页）
    @GetMapping("auth/{page}/{limit}")
    public Result<Object> list(@PathVariable Long page,
                               @PathVariable Long limit,
                               OrderQueryVo orderQueryVo,
                               HttpServletRequest request){
//        设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }

//    获取订单状态
    @GetMapping("auth/getStatusList")
    public Result<Object> getStatusList(){
        List<Map<String, Object>> statusList = OrderStatusEnum.getStatusList();
        return Result.ok(statusList);
    }
    @GetMapping("insertOne")
    public Result<Object> insertOne(){
        boolean isInsert = orderService.insertOne();
        return Result.ok(isInsert);
    }

//    根据订单id查询订单详情
    @GetMapping("auth/getOrders/{orderId}")
    public Result<Object> getOrders(@PathVariable String orderId){
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }
}
