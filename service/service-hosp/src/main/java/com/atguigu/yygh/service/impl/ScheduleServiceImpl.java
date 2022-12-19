package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.hosp.BookingRule;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.repository.ScheduleRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.service.ScheduleService;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        resMap.put("bookingScheduleList", bookingScheduleRuleVosList);
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
        // 把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.stream().forEach(item -> {
            this.packageSchedule(item);
        });
        return scheduleList;
    }

    //    获取可预约排班数据
    @Override
    public Map<String, Object> getBookingSchduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        HashMap<String, Object> map = new HashMap<>();
//        获取预约规则
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if(null == hospital){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
//        获取可预约日期分页数据
        IPage iPage = this.getListDate(page, limit, bookingRule);
//        当前页可预约日期
        List<Date> dateList = iPage.getRecords();
//        获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode)
                .and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();
//        获取科室剩余预约数

//        合并数据
        HashMap<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleVoList)){
//           原始导入出错是因为使用的方法不对
            Map<Date, BookingScheduleRuleVo> collect = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
//        获取可预约排班规则
        ArrayList<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len ; i++) {
            Date date = dateList.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if(null == bookingScheduleRuleVo){// 说明当天没有排班医生
                BookingScheduleRuleVo bookingScheduleRuleVo1 = new BookingScheduleRuleVo();
//                就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
//                科室剩余数 -1 表示无号
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
//            计算当前预约日期为星期几
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
//            最后一页最后一条记录为即将预约状态 0， 正常 1， 即将放号 -1。 当天已停止挂号
            if(i == len - 1 && page == iPage.getPages()){
                bookingScheduleRuleVo.setStatus(1);
            }else{
                bookingScheduleRuleVo.setStatus(0);
            }
//            当天预约如果过了停号时间，不能预约
            if(i == 0 && page == 1){
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()){
//                    停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }
//        可预约日期规则数据
        map.put("bookingScheduleList", bookingScheduleRuleVoList);
        map.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getHospName(hoscode));
        //科室
        Department department =departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
//月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
//放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
//停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        map.put("baseMap", baseMap);
        return map;
    }

    private IPage getListDate(Integer page, Integer limit, BookingRule bookingRule) {
//        当天放号时间
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
//        预约周期
        Integer cycle = bookingRule.getCycle();
//        如果当天放号时间已过，则预约周期后一天为即将放号时间，周期+1
        if(releaseTime.isBeforeNow()){
            cycle += 1;
        }
//        可预约所有日期，最后一天显示即将放号倒计时
        ArrayList<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
//            计算当前预约日期
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
//        日期分页，由于预约周期不一样，页面一排最多显示7个，多了则分页显示
        ArrayList<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        if(end > dateList.size()){
            end = dateList.size();
        }
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, 7, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + "" + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    // 封装排班详情其他值 医院名称/科室名称/日期对应星期
    private void packageSchedule(Schedule item) {
        // 设置医院名称
        item.getParam().put("hosname", hospitalService.getHospName(item.getHoscode()));
        // 设置科室名称
        item.getParam().put("depname", departmentService.getDepName(item.getHoscode(), item.getDepcode()));
        // 设置日期对应星期
        item.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(item.getWorkDate())));
    }

}
