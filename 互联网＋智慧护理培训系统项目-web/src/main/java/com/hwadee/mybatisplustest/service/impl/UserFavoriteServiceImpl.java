package com.hwadee.mybatisplustest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwadee.mybatisplustest.entity.UserFavorite;
import com.hwadee.mybatisplustest.mapper.UserFavoriteMapper;
import com.hwadee.mybatisplustest.service.UserFavoriteService;
import org.springframework.stereotype.Service;

@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite> implements UserFavoriteService {
}







