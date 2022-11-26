package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.repository.DepartmentRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public void save(Map<String, Object> paramMap) {
        String departmentString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(departmentString, Department.class);
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if(null != departmentExist){
//            copy不为null的值，该方法为自定义方法
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

//   查询科室接口
    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable =  PageRequest.of(page - 1, limit);
        Department department = new Department();
//        创建适配器，如何使用查询条件
        ExampleMatcher matching = ExampleMatcher.matching()//构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//改变默认字符
                .withIgnoreCase(true);//改变默认大小写忽略方式：忽略大小写
//        创建实例
        Example<Department> example = Example.of(department, matching);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null != department){
            departmentRepository.deleteById(department.getId());
        }

    }
}
