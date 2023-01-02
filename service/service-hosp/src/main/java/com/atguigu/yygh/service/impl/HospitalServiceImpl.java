package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.core.HostReactor;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.repository.HospitalRepository;
import com.atguigu.yygh.service.HospitalService;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
//        把参数map集合转换对象 Hospital
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Hospital.class);
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
//        判断是否存在数据
//        如果存在数据，进行修改
        if( hospitalExist != null){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
//        如果不存在，添加数据
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }


    //    条件查询分页
    @Override
    public Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo) {
//        创建pageable对象
        Pageable pageable = PageRequest.of(page - 1, limit);
//        创建条件适配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
//        hospitalSetQueryVo转换hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        String s = pages.toString();
        // System.out.println(s);
//        获取查询list集合，遍历进行医院等级封装
        pages.getContent().stream().forEach(item ->{
            this.setHostpitalHosType(item);
        });
        return pages;
    }

//    update the hosp status
    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }


    //        获取查询list集合，遍历进行医院等级封装
    private Hospital setHostpitalHosType(Hospital item) {
//        System.out.println(item.toString());
//        根据dictcode和value获取医院等级名称
//        System.out.println(item.getCityCode());
        String hostypeString = dictFeignClient.getName("Hostype", item.getHostype());
//        查询省市区
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        String cityString = dictFeignClient.getName(item.getCityCode());
        String districtString = dictFeignClient.getName(item.getDistrictCode());

        item.getParam().put("fullAddress", provinceString + cityString + districtString);
        item.getParam().put("hostype", hostypeString);
        return item;
    }

    // 医院详情信息
    @Override
    public Map<String, Object> getHospById(String id) {
        // TODO Auto-generated method stub
        Hospital hospital = hospitalRepository.findById(id).get();
        Hospital hospital2 = this.setHostpitalHosType(hospital);
        HashMap<String,Object> map = new HashMap<>();
        // 医院基本信息（包含医院等级）
        map.put("hospital", hospital2);
        // 单独处理
        map.put("bookingRule", hospital2.getBookingRule());
        // 不需要重复返回
        hospital2.setBookingRule(null);
        return map;
    }

    // 获取医院名称
    @Override
    public String getHospName(String hoscode) {
        // TODO Auto-generated method stub
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        // 如果不为空返回数据
        if(hospitalByHoscode != null){
            return hospitalByHoscode.getHosname();
        }
        return null;
    }

    //    根据医院名称查询
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //    根据医院编号获取科室
    @Override
    public Map<String, Object> item(String hoscode) {
        HashMap<String, Object> map = new HashMap<>();
        Hospital hospital = this.setHostpitalHosType(this.getByHoscode(hoscode));
        map.put("hospital", hospital);
        map.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return map;
    }
}
