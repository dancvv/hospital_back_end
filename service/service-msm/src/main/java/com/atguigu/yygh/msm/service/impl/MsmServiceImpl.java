package com.atguigu.yygh.msm.service.impl;


import com.atguigu.yygh.msm.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
//        判断手机号是否为空
        if(!StringUtils.hasLength(phone)){
           return  false;
        }
        return false;
    }
}
