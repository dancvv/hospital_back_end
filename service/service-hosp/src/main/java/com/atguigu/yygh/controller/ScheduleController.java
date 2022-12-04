package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
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
}
