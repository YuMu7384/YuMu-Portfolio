package com.hwadee.mybatisplustest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwadee.mybatisplustest.entity.Notification;
import com.hwadee.mybatisplustest.mapper.NotificationMapper;
import com.hwadee.mybatisplustest.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
}







