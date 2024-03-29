package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

//    医院列表
    @GetMapping("/list/{page}/{limit}")
    public Result<Object> listHosp(@PathVariable int page,
                                   @PathVariable int limit,
                                   HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageMoldel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageMoldel);
    }
//    update the hospital status
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result<Object> updateHospStatus(@PathVariable String id, @PathVariable Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();

    }
    // 医院详情信息
    @GetMapping("showHospDetail/{id}")
    public Result<Object> showHospDetail(@PathVariable String id){
        Map<String, Object> hospital = hospitalService.getHospById(id);
        return Result.ok(hospital);
    }
}
