package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.yygh.oss.Utils.ConstantOssProperties;
import com.atguigu.yygh.oss.service.FileService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    //    上传文件到阿里云
    @Override
    public String upload(MultipartFile file) {
//        Endpoint 以杭州为例， 其他region按实际情况填写
        String endpoint = ConstantOssProperties.ENDPOINT;
        String accessKeyId = ConstantOssProperties.ACCESS_KEY_ID;
        String secret = ConstantOssProperties.SECRET;
        String bucket = ConstantOssProperties.BUCKET;
        try {
//            创建实例
//        上传文件流
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secret);
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
//            生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
//            按照当前日期，创建文件夹，上传到创建文件夹里面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;
            System.out.println("fileName" + fileName);
//            调用方法实现上传
            ossClient.putObject(bucket, fileName, inputStream);
//            关闭
            ossClient.shutdown();
//            上传之后文件路径
            String url = "https://" + bucket + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
