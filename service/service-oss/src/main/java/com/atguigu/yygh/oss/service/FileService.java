package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
//    上传文件到阿里云
    String upload(MultipartFile file);

}
