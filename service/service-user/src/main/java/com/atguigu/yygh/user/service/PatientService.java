package com.atguigu.yygh.user.service;

import com.atguigu.yygh.model.user.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PatientService extends IService<Patient> {
//    查询所有userid
    List<Patient> findAllUserId(Long userId);

    Patient getPatientById(Long id);
}
