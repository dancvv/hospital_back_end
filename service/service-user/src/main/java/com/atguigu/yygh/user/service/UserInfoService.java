package com.atguigu.yygh.user.service;

import com.atguigu.yygh.vo.user.LoginVo;

import java.util.Map;

public interface UserInfoService {
    //    用户手机号登陆接口
    Map<String, Object> loginUser(LoginVo loginVo);
}
