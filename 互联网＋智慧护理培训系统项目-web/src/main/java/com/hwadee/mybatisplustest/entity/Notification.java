package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("type")
    private String type; // 通知类型：achievement, learning_reminder, system等
    
    @TableField("title")
    private String title; // 通知标题
    
    @TableField("content")
    private String content; // 通知内容
    
    @TableField("icon")
    private String icon; // 通知图标
    
    @TableField("is_read")
    private Boolean isRead; // 是否已读
    
    @TableField("read_at")
    private LocalDateTime readAt; // 阅读时间
    
    @TableField("created_at")
    private LocalDateTime createdAt;
}







