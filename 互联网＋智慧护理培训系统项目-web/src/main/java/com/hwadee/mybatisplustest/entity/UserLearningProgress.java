package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_learning_progress")
public class UserLearningProgress {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("article_id")
    private Long articleId;
    
    @TableField("video_id")
    private Long videoId;
    
    @TableField("progress_percent")
    private Integer progressPercent; // 0-100，学习进度百分比
    
    @TableField("is_completed")
    private Boolean isCompleted; // 是否完成学习
    
    @TableField("started_at")
    private LocalDateTime startedAt; // 开始学习时间
    
    @TableField("completed_at")
    private LocalDateTime completedAt; // 完成学习时间
    
    @TableField("last_studied_at")
    private LocalDateTime lastStudiedAt; // 最后学习时间
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}







