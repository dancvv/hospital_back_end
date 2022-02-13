package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

//    1.测试，查询信息
    @Operation(summary = "查询医院")
    @GetMapping("findAll")
    public Result<Object> findAllHospitalSet(){
//        调用service的方法
//        返回的是json数据，默认做的转换
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }
//    2.删除医院逻辑设置
    @Operation(summary = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result<Object> removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    
//    3 条件查询带分页
//    通过vo中的查询实体，封装条件值
//    获取条件对象
//    分页数据（当前页，限制）

    /**
     * @RequestBody(required = false)后面的内容可以为空，与之对应的为postmapping
     */
    @PostMapping("findPage/{current}/{limit}")
    public Result<Object> findPageHospSet(@PathVariable long current,
                                          @PathVariable long limit,
                                          @RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
//        创建page对象，当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
//        构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalQueryVo.getHoscode();
        String hosname = hospitalQueryVo.getHosname();
//        入参检验是否为空
        if (!StringUtils.hasText(hosname)){
            wrapper.like("hosname",hospitalQueryVo.getHosname());
        }
        if (!StringUtils.hasText(hoscode)){
            wrapper.eq("hoscode",hospitalQueryVo.getHoscode());
        }
//        此处报错是由于wrapper条件错误的写为object
        Page<HospitalSet> page1 = hospitalSetService.page(page,wrapper);
        return Result.ok(page1);
    }
    
//    4 添加医院设置
    
//    5 根据id获取医院设置
    
//    6 修改医院设置
    
//    7 
}
