package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article_view")
public class ArticleView {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("article_id")
    private Long articleId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("view_count")
    private Integer viewCount;
    
    @TableField("last_viewed_at")
    private LocalDateTime lastViewedAt;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}







