package com.atguigu.yygh.controller.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;
//    查询医院列表
    @GetMapping("findHospList/{page}/{limit}")
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
    //    根据医院编号获取科室
    @GetMapping("department/{hoscode}")
    public Result<Object> index(@PathVariable String hoscode){
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Result.ok(deptTree);
    }
    //    根据医院编号获取科室
    @GetMapping("/findHospDetail/{hoscode}")
    public Result<Object> item(@PathVariable String hoscode){
        Map<String, Object> map =  hospitalService.item(hoscode);
        return Result.ok(map);
    }

    //    find by hosp serial number and department number
//    query the schedule rules data
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Object> getScheduleRule(@PathVariable long page,
                                          @PathVariable long limit,
                                          @PathVariable String hoscode,
                                          @PathVariable String depcode){
        Map<String, Object> resMap = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        System.out.println("resMap:" + resMap);
        return Result.ok(resMap);
    }
    // 根据医院编号、科室编号和工作日期，查询排班详细信息
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result<Object> getScheduleDetail(@PathVariable String hoscode,
                                            @PathVariable String depcode,
                                            @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return Result.ok(list);
    }
}
