package com.atguigu.yygh.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.service.HospitalSetService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

//@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private HospitalSetService hospitalSetService;

//    获取可预约排班数据
    @GetMapping("auth/getBookingSchedule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Object> getBookingSchedule(@PathVariable Integer page,
                                             @PathVariable Integer limit,
                                             @PathVariable String hoscode,
                                             @PathVariable String depcode){
        Map<String, Object> resMap = scheduleService.getBookingSchduleRule(page, limit, hoscode, depcode);
        return Result.ok(resMap);
    }

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
     * 此处加上body内容会出现报错，暂无法清除细节原因
     */
    @PostMapping("findPage/{current}/{limit}")
    public Result<Object> findPageHospSet(@PathVariable long current,
                                          @PathVariable long limit,
                                          @RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
//        @RequestBody(required = false) HospitalQueryVo hospitalQueryVo
//        创建page对象，当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
//        构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hoscode = hospitalQueryVo.getHoscode();
        String hosname = hospitalQueryVo.getHosname();
//        入参检验是否为空，这里的逻辑出现过问题
//        由于之前采用的是isEmpty()方法，现在采用hasText()方法
//        使用方法完全一致
        if (StringUtils.hasText(hosname)){
            wrapper.like("hosname",hospitalQueryVo.getHosname());
        }
        if (StringUtils.hasText(hoscode)){
            wrapper.eq("hoscode",hospitalQueryVo.getHoscode());
        }
//        此处报错是由于wrapper条件错误的写为object
        Page<HospitalSet> page1 = hospitalSetService.page(page,wrapper);
        return Result.ok(page1);
    }
    
//    4 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
//        设置状态 1使用 0不能使用
        hospitalSet.setStatus(1);
//        签名秘钥
        Random random =new Random();
//        根据当前时间进行加密
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

//        调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }
    
//    5 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){
//        模拟异常
//        try {
//            int a=1/0;
//        }catch (Exception e){
////            手动抛出异常
//            throw new YyghException("失败",201);
//        }
        HospitalSet byId = hospitalSetService.getById(id);
        return Result.ok(byId);
    }
//    6 修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    
//    7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean flag = hospitalSetService.removeByIds(idList);
        if(flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

//    8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //    根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
//        设置状态
        hospitalSet.setStatus(status);
//        调用方法
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

//    9 发送签名秘钥
    @PutMapping("sendKey{id}")
    public Result lockHospitalSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
//        查询秘钥及医院编号
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
//        TODO 发送短信
        return  Result.ok();
    }
}
