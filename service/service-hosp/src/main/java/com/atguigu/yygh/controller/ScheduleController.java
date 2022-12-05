package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

//    find by hosp serial number and department number
//    query the schedule rules data
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Object> getScheduleRule(@PathVariable long page,
                                          @PathVariable long limit,
                                          @PathVariable String hoscode,
                                          @PathVariable String depcode){
        Map<String, Object> resMap = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        return Result.ok(resMap);
    }
    // 根据医院编号、科室编号和工作日期，查询排班详细信息
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result<Object> getScheduleDetail(@PathVariable String hoscode, @PathVariable String depcode, @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return Result.ok(list);
    }
}
