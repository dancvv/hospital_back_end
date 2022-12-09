package com.atguigu.yygh.msm.service.impl;


import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.core.http.HttpMethod;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.ConstantPropertiesUtils;
import darabonba.core.RequestConfiguration;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
//        判断手机号是否为空
        if(!StringUtils.hasLength(phone)){
           return  false;
        }
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ConstantPropertiesUtils.ACCESS_KEY_ID)
                .accessKeySecret(ConstantPropertiesUtils.SECRET)
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-dalian")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration
                                .create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .requestConfiguration(RequestConfiguration.create()
                        .setHttpMethod(HttpMethod.POST))
                .phoneNumbers(phone)
                .signName("我的java网站学习项目")
                .templateCode("SMS_264155029")

                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        return false;
    }
}
