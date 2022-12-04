package com.atguigu.yygh.repository;

import com.atguigu.yygh.model.hosp.Schedule;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {

    // 根据医院编号和排班编号查询
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    // 根据医院编号、科室编号和工作日期，查询排班详细信息
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date date);
}
