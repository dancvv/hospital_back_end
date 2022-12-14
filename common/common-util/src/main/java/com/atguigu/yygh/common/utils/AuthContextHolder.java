package com.atguigu.yygh.common.utils;

import com.atguigu.yygh.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

// get the current user info
public class AuthContextHolder {
    public static Long getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }
    public static String getUserName(HttpServletRequest request){
        String token = String.valueOf(request.getHeaders("token"));
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
