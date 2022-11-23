package com.atguigu.yygh.service.impl;

import com.atguigu.yygh.repository.HospitalRepository;
import com.atguigu.yygh.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
}
