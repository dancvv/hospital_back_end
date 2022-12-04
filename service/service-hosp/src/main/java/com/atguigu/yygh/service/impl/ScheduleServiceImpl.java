package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.repository.ScheduleRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    // 上传排班接口
    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);
        // 根据医院编号和排班编号查询
        Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),
                schedule.getHosScheduleId());
        if (null != scheduleExist) {
            // copy不为null的值，该方法为自定义方法
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    // 查询排班接口
    @Override
    public Page<Schedule> selectPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        // 创建example对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);
        // 创建适配器，如何使用查询条件
        ExampleMatcher matching = ExampleMatcher.matching()// 构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)// 改变默认字符
                .withIgnoreCase(true);// 改变默认大小写忽略方式：忽略大小写
        // 创建实例
        Example<Schedule> example = Example.of(schedule, matching);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    // 删除排班
    @Override
    public void remove(String hoscode, String hosScheduleId) {
        // 查询数据库
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (null != schedule) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    // 根据医院编号 和 科室编号 ，查询排班规则数据
    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        // TODO Auto-generated method stub
        // 1 根据医院编号 和 科室编号 查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        // 2 根据工作日workDate期进行分组
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria), // 匹配条件
                Aggregation.group("workDate")// 分组字段
                        .first("workDate").as("workDate")
                        // 3 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // 排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                // 4 实现分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit));
        // 调用方法，最终执行
        AggregationResults<BookingScheduleRuleVo> aggResults = mongoTemplate.aggregate(agg, Schedule.class,
                BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVosList = aggResults.getMappedResults();
        // 分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                    Aggregation.match(criteria),
                    Aggregation.group("workDate")
                    );
        AggregationResults<BookingScheduleRuleVo> totalAggResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        // 总记录数
        int size = totalAggResults.getMappedResults().size();
        // 把日期对应的星期获取
        for(BookingScheduleRuleVo bookingScheduleRuleVos:bookingScheduleRuleVosList){
             Date workDate = bookingScheduleRuleVos.getWorkDate();
             String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
             bookingScheduleRuleVos.setDayOfWeek(dayOfWeek);
        }
        // 设置最终数据，返回
        HashMap<String,Object> resMap = new HashMap<String, Object>();
        resMap.put("bookingScheduleRuleVosList", bookingScheduleRuleVosList);
        resMap.put("total", size);
        // 获取医院名称
        String hosname = hospitalService.getHospName(hoscode);
        HashMap<String,Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hosname);
        resMap.put("baseMap", baseMap);
        return resMap;
    }
    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

    // 根据医院编号、科室编号和工作日期，查询排班详细信息
    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        // TODO Auto-generated method stub
        // 根据参数查询mongodb
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        // 把得到listj集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.stream().forEach(item -> {
            this.packageSchedule(item);
        });
        return scheduleList;
    }

    // 封装排班详情其他值 医院名称/科室名称/日期对应星期
    private void packageSchedule(Schedule item) {
        System.out.println(item.toString());
        // 设置医院名称
        item.getParam().put("hosname", hospitalService.getHospName(item.getHoscode()));
        // 设置科室名称
        item.getParam().put("depname", departmentService.getDepName(item.getHoscode(), item.getDepcode()));
        // 设置日期对应星期
        item.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(item.getWorkDate())));
    }

}
