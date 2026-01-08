package com.hwadee.mybatisplustest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwadee.mybatisplustest.entity.Admin;
import com.hwadee.mybatisplustest.mapper.AdminMapper;
import com.hwadee.mybatisplustest.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}


