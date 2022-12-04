package com.atguigu.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.repository.DepartmentRepository;
import com.atguigu.yygh.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //    根据医院编号，查询医院所有科室列表
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
//        创建list集合，用于最终数据封装
        ArrayList<DepartmentVo> result = new ArrayList<>();
//        根据医院编号，查询医院所有科室信息
        Department departmentQuerry = new Department();
        departmentQuerry.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuerry);
//        所有科室信息
        List<Department> all = departmentRepository.findAll(example);

//        根据大科室编号分组 , 获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
//        遍历map集合
        for(Map.Entry<String, List<Department>> entry: departmentMap.entrySet()){
//            大科室编号
            String bigcode = entry.getKey();
//            大科室编号对应的全部数据
            List<Department> departmentList = entry.getValue();


//            封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(departmentList.get(0).getBigname());

//            封装小科室
            ArrayList<DepartmentVo> children = new ArrayList<>();
            for(Department department:departmentList){
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
//                封装到list集合
                children.add(departmentVo2);
            }
//            把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
//            放到最终result里面
            result.add(departmentVo1);
        }
        return result;
    }

    // 根据科室编号，和医院编号，查询科室名称
    @Override
    public String getDepName(String hoscode, String depcode) {
        // TODO Auto-generated method stub
        Department department =  departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department != null){
            return department.getDepname();
        }
        return null;
    }

}
