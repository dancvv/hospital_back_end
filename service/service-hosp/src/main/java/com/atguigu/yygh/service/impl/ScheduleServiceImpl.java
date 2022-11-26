package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.repository.ScheduleRepository;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

//    上传排班接口
    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);
//        根据医院编号和排班编号查询
        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if(null != scheduleExist){
//            copy不为null的值，该方法为自定义方法
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        }else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

//    查询排班接口
    @Override
    public Page<Schedule> selectPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable =  PageRequest.of(page - 1, limit);
//        创建example对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);
//        创建适配器，如何使用查询条件
        ExampleMatcher matching = ExampleMatcher.matching()//构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//改变默认字符
                .withIgnoreCase(true);//改变默认大小写忽略方式：忽略大小写
//        创建实例
        Example<Schedule> example = Example.of(schedule, matching);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

//   删除排班
    @Override
    public void remove(String hoscode, String hosScheduleId) {
//        查询数据库
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(null != schedule){
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
