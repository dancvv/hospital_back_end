package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {

//        构建数据list集合
        List<UserData> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("user "+i);
            list.add(userData);
        }

//        设置文件路径和文件名称
        String fileName = "/Users/weivang/DataVolume/excel/01.xlsx";
//        调用方法实现写操作
        EasyExcel.write(fileName, UserData.class).sheet("用户信息表").doWrite(list);
    }
}
