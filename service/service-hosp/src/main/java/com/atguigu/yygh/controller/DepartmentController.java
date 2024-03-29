package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

//    根据医院编号，查询医院所有科室列表
    @GetMapping("getDeptList/{hoscode}")
    public Result<Object> getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}
