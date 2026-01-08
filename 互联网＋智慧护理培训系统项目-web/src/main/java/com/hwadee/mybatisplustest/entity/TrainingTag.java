// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.entity;

// 导入MyBatis-Plus的主键类型枚举
import com.baomidou.mybatisplus.annotation.IdType;
// 导入MyBatis-Plus的主键注解
import com.baomidou.mybatisplus.annotation.TableId;
// 导入MyBatis-Plus的表名注解
import com.baomidou.mybatisplus.annotation.TableName;

// 导入Java日期类
import java.util.Date;

/**
 * 培训标签实体类
 * 
 * 功能：存储培训标签的信息，用于对文章、视频、PPT进行标签分类
 * 对应数据库表：training_tag
 */
@TableName("training_tag")  // 指定对应的数据库表名
public class TrainingTag {

    @TableId(type = IdType.AUTO)  // 主键，自增长
    private Long id;  // 标签ID

    private String name;  // 标签名称

    private Integer status;  // 标签状态：1-启用 0-禁用

    private Date createdAt;  // 创建时间

    private Date updatedAt;  // 更新时间

    /**获取标签ID*/
    public Long getId() {
        return id;  // 返回标签ID
    }

    /**设置标签ID*/
    public void setId(Long id) {
        this.id = id;  // 设置标签ID
    }

    /**获取标签名称*/
    public String getName() {
        return name;  // 返回标签名称
    }

    /**设置标签名称*/
    public void setName(String name) {
        this.name = name;  // 设置标签名称
    }

    /**获取标签状态*/
    public Integer getStatus() {
        return status;  // 返回标签状态
    }

    /**设置标签状态*/
    public void setStatus(Integer status) {
        this.status = status;  // 设置标签状态
    }

    /**获取创建时间*/
    public Date getCreatedAt() {
        return createdAt;  // 返回创建时间
    }

    /**设置创建时间*/
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;  // 设置创建时间
    }

    /**获取更新时间*/
    public Date getUpdatedAt() {
        return updatedAt;  // 返回更新时间
    }

    /**设置更新时间*/
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;  // 设置更新时间
    }
}  // TrainingTag类结束







