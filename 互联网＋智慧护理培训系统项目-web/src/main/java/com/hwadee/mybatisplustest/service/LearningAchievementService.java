package com.hwadee.mybatisplustest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwadee.mybatisplustest.entity.LearningAchievement;

public interface LearningAchievementService extends IService<LearningAchievement> {
    void checkAndUnlockAchievements(Long userId);
}







