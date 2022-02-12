package com.atguigu.yygh.controller;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

//    1.测试，查询信息
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet(){
//        调用service的方法
//        返回的是json数据，默认做的转换
        return hospitalSetService.list();
    }
//    2.删除医院逻辑设置
    @DeleteMapping("{id}")
    public boolean removeHospSet(@PathVariable Long id){
        return hospitalSetService.removeById(id);
    }
}
