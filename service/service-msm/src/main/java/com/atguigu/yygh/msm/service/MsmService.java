package com.atguigu.yygh.msm.service;

import java.util.Map;

public interface MsmService {

//    发送手机验证码
    boolean send(String phone, String code);
}
