package com.atguigu.yygh.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;

//    查询医院列表
    @GetMapping("findHospList/{page}/{list}")
    public Result<Object> findHospList(@PathVariable int page,
                                       @PathVariable int limit,
                                       HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }
//    根据医院名称查询
    @GetMapping("findByHosname/{hosname}")
    public Result<Object> findByHosname(@PathVariable String hosname){
        List<Hospital> list =  hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }
}
