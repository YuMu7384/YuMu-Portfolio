package com.hwadee.mybatisplustest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwadee.mybatisplustest.entity.LearningAchievement;
import com.hwadee.mybatisplustest.entity.Notification;
import com.hwadee.mybatisplustest.entity.UserLearningProgress;
import com.hwadee.mybatisplustest.mapper.LearningAchievementMapper;
import com.hwadee.mybatisplustest.service.LearningAchievementService;
import com.hwadee.mybatisplustest.service.NotificationService;
import com.hwadee.mybatisplustest.service.UserLearningProgressService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LearningAchievementServiceImpl extends ServiceImpl<LearningAchievementMapper, LearningAchievement> implements LearningAchievementService {

    @Resource
    private UserLearningProgressService progressService;

    @Resource
    private NotificationService notificationService;

    @Override
    public void checkAndUnlockAchievements(Long userId) {
        // 获取用户学习进度
        LambdaQueryWrapper<UserLearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLearningProgress::getUserId, userId);
        List<UserLearningProgress> progressList = progressService.list(wrapper);

        long completedArticles = progressList.stream()
                .filter(p -> p.getArticleId() != null && Boolean.TRUE.equals(p.getIsCompleted()))
                .count();

        // 检查已解锁的成就
        LambdaQueryWrapper<LearningAchievement> achievedWrapper = new LambdaQueryWrapper<>();
        achievedWrapper.eq(LearningAchievement::getUserId, userId);
        List<LearningAchievement> unlocked = this.list(achievedWrapper);
        Map<String, LearningAchievement> unlockedMap = new HashMap<>();
        unlocked.forEach(a -> unlockedMap.put(a.getAchievementType(), a));

        LocalDateTime now = LocalDateTime.now();

        // 检查各种成就
        if (completedArticles >= 1 && !unlockedMap.containsKey("first_article")) {
            unlockAchievement(userId, "first_article", "首次完成", "完成了第一篇文章的学习", "🎉", 10, now);
        }

        if (completedArticles >= 5 && !unlockedMap.containsKey("halfway")) {
            unlockAchievement(userId, "halfway", "半程达成", "完成了5篇文章的学习", "📚", 30, now);
        }

        if (completedArticles >= 10 && !unlockedMap.containsKey("completed_all")) {
            unlockAchievement(userId, "completed_all", "完美完成", "完成了所有10篇文章的学习", "🏆", 100, now);
        }
    }

    private void unlockAchievement(Long userId, String type, String name, String desc, String icon, Integer points, LocalDateTime unlockedAt) {
        LearningAchievement achievement = new LearningAchievement();
        achievement.setUserId(userId);
        achievement.setAchievementType(type);
        achievement.setAchievementName(name);
        achievement.setAchievementDesc(desc);
        achievement.setIcon(icon);
        achievement.setPoints(points);
        achievement.setUnlockedAt(unlockedAt);
        achievement.setCreatedAt(unlockedAt);
        this.save(achievement);

        // 创建成就通知
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("achievement");
        notification.setTitle("🏆 解锁新成就！");
        notification.setContent(String.format("恭喜您解锁成就：%s (%s)", name, desc));
        notification.setIcon(icon);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationService.save(notification);
    }
}

