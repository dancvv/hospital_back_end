package com.atguigu.yygh.oss.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    private FileService fileService;
//    上传文件到阿里云
    @PostMapping("fileUpload")
    public Result<Object> fileUpload(MultipartFile file){
//        上传文件到阿里云oss
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
