package com.atguigu.yygh.user.api;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.AuthContextHolder;
import com.atguigu.yygh.model.user.Patient;
import com.atguigu.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// 就诊人管理接口
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

//    就诊人列表
    @GetMapping("auth/findAll")
    public Result<Object> findAll(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

//    添加就诊人
    @PostMapping("auth/save")
    public Result<Object> savePatient(@RequestBody Patient patient, HttpServletRequest request){
//        获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

//    根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result<Object> getPatient(@PathVariable Long id){
        Patient patient = patientService.getPatientById(id);
        return Result.ok(patient);
    }

//    修改就诊人
    @PostMapping("auth/update")
    public Result<Object> updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.ok();
    }

//    删除就诊人
    @DeleteMapping("/auth/remove/{id}")
    public Result<Object> removePatient(@PathVariable Long id){
        patientService.removeById(id);
        return Result.ok();
    }

}
