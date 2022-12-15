package com.atguigu.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.user.utils.ConstantPropertiesUtils;
import com.atguigu.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
public class WwxbApiController {
    @Autowired
    private UserInfoService userInfoService;
//    微信扫描后回调的方法
    @GetMapping("callback")
    public String callback(String code, String state){
//        第一步获取临时票据code
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);

        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
//            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

//        第二部拿着code和微信id和密钥，请求微信固定地址，得到两个值
//        使用code和appid以及appscrect 换取access_token
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtils.WX_OPEN_APP_ID,
                ConstantPropertiesUtils.WX_OPEN_APP_SECRET,
                code);
//        使用httpclient 请求这个地址
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo: " + accessTokenInfo);
//            从返回字符串获取两个值 openid 和access_token
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            Object access_token = jsonObject.get("access_token");
            String openid = jsonObject.getString("openid");

//            判断数据库是否存在微信的扫描人信息
//            根据openid判断
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            if (null == userInfo){
//                数据里不存在微信信息
//            第三步拿着 openid 和 access_token 请求微信地址，得到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo: " + resultInfo);
                JSONObject resultJsonObject = JSONObject.parseObject(resultInfo);
//            解析用户信息
                String nickname = resultJsonObject.getString("nickname");
                String headimgurl = resultJsonObject.getString("headimgurl");

//            获取扫描人信息添加数据库
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }


//            返回name和token字符串
            HashMap<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
//            判断userInfo是否有手机号，如果手机号为空，返回openid
//            如果手机号不为空，返回openid值是空字符串
//            前端判断：手机号是否需要绑定
            if(!StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            System.out.println("token: " + token);
            map.put("token", token);
//            跳转到前端页面上
            return "redirect:"
                    + ConstantPropertiesUtils.YYGH_BASE_URL
                    + "/weixin/callback?token="
                    +map.get("token")
                    +"&openid="
                    +map.get("openid")
                    +"&name="
                    +URLEncoder.encode(name, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
//    1生成微信扫描二维码
//    返回生成二维码需要参数
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result<Object> genOrConnect(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", ConstantPropertiesUtils.WX_OPEN_APP_ID);
        String wxOpenRedirectUrl = ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL;
        String encode = URLEncoder.encode(wxOpenRedirectUrl, StandardCharsets.UTF_8);
        map.put("redirectUri", encode);
        map.put("scope",  "snsapi_login");
        map.put("state", System.currentTimeMillis());
        return Result.ok(map);
    }
}
