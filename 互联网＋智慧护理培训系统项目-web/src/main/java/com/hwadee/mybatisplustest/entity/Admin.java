package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("admin_no")
    private String adminNo;
    
    private String username;
    private String password;
    
    @TableField("real_name")
    private String realName;
    
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}


