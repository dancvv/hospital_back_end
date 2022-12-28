package com.atguigu.yygh.user.client;

import com.atguigu.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-user")
@Repository
public interface PatientFeignClient {

//    获取就诊人
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientOrder(@PathVariable Long id);
}
