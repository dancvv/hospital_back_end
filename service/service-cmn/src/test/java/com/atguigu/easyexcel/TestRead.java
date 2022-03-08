package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        //        设置文件路径和文件名称
        String fileName = "/Users/weivang/DataVolume/excel/01.xlsx";
//        调用方法实现写操作
        EasyExcel.read(fileName, UserData.class, new ExcelListener()).sheet().doRead();
    }
}
