package com.hwadee.mybatisplustest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwadee.mybatisplustest.entity.Patient;
import com.hwadee.mybatisplustest.mapper.PatientMapper;
import com.hwadee.mybatisplustest.service.PatientService;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
}


