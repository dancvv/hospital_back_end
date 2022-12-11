package com.atguigu.yygh.user.api.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.user.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
public class WwxbApiController {
//    1生成微信扫描二维码
//    返回生成二维码需要参数
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result<Object> genOrConnect(){
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("appid", ConstantPropertiesUtils.WX_OPEN_APP_ID);
            String wxOpenRedirectUrl = ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL;
            String encode = URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirectUri", encode);
            map.put("scope",  "snsapi_login");
            map.put("state", System.currentTimeMillis());
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
