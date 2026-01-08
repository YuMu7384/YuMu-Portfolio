package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_achievement")
public class LearningAchievement {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("achievement_type")
    private String achievementType; // 成就类型：first_article, completed_10, perfect_streak等
    
    @TableField("achievement_name")
    private String achievementName; // 成就名称
    
    @TableField("achievement_desc")
    private String achievementDesc; // 成就描述
    
    @TableField("icon")
    private String icon; // 成就图标
    
    @TableField("points")
    private Integer points; // 成就积分
    
    @TableField("unlocked_at")
    private LocalDateTime unlockedAt; // 解锁时间
    
    @TableField("created_at")
    private LocalDateTime createdAt;
}







