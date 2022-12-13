package com.atguigu.yygh.msm.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.core.http.HttpMethod;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.ConstantPropertiesUtils;
import darabonba.core.RequestConfiguration;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
//        判断手机号是否为空
        if(!StringUtils.hasLength(phone)){
//           return  false;
        }
//        配置密钥信息
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ConstantPropertiesUtils.ACCESS_KEY_ID)
                .accessKeySecret(ConstantPropertiesUtils.SECRET)
                .build());
//        配置客户端
        AsyncClient client = AsyncClient.builder()
                .region("cn-dalian")
                .credentialsProvider(provider)
                .overrideConfiguration(
//                        客户端设置
                        ClientOverrideConfiguration
                                .create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
//        api 请求设置
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
//        发送短信
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .requestConfiguration(RequestConfiguration.create()
                        .setHttpMethod(HttpMethod.POST)
                )
//                设置参数 手机号 签名 code 验证码
                .phoneNumbers(phone)
                .signName("我的java网站学习项目")
                .templateCode("SMS_264155029")
                .templateParam(JSONObject.toJSONString(param))
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        HashMap<String, Object> map = new HashMap<>();
        try {
            SendSmsResponse sendSmsResponse = response.get();
            SendSmsResponseBody body = sendSmsResponse.getBody();
            boolean isOk = body.getMessage().equals("OK");
            client.close();
            return isOk;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
//
}
