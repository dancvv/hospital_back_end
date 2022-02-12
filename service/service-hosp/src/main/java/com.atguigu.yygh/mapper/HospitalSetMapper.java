package com.atguigu.yygh.mapper;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//跨包引用需要提前导入
//mapper报错是因为没有加入相应的扫描注解

public interface HospitalSetMapper extends BaseMapper<HospitalSet> {

}
