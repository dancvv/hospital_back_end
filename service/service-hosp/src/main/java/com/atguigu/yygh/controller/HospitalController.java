package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
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
    @GetMapping("/list2/{page}/{limit}")
    public Result<Object> listHosp2(@PathVariable int page,
                                   @PathVariable int limit,
                                   HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageMoldel = hospitalService.getPages(page, limit, hospitalQueryVo);
        return Result.ok(pageMoldel);
    }
}
