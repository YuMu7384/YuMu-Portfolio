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
 * 培训文章实体类
 * 
 * 功能：存储培训文章的信息，包括文章内容、分类、标签等
 * 对应数据库表：training_article
 */
@TableName("training_article")  // 指定对应的数据库表名
public class TrainingArticle {

    @TableId(type = IdType.AUTO)  // 主键，自增长
    private Long id;  // 文章ID

    private String title;  // 文章标题

    private String coverUrl;  // 封面图片URL

    private Long categoryId;  // 所属分类ID

    private String tagIds;  // 标签ID列表（逗号分隔）

    private String content;  // 文章内容（富文本HTML）

    private Integer publishStatus;  // 发布状态：0-草稿 1-已发布

    private Date publishAt;  // 发布时间

    private Date createdAt;  // 创建时间

    private Date updatedAt;  // 更新时间

    /**获取文章ID*/
    public Long getId() {
        return id;  // 返回文章ID
    }

    /**设置文章ID*/
    public void setId(Long id) {
        this.id = id;  // 设置文章ID
    }

    /**获取文章标题*/
    public String getTitle() {
        return title;  // 返回文章标题
    }

    /**设置文章标题*/
    public void setTitle(String title) {
        this.title = title;  // 设置文章标题
    }

    /**获取封面图片URL*/
    public String getCoverUrl() {
        return coverUrl;  // 返回封面图片URL
    }

    /**设置封面图片URL*/
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;  // 设置封面图片URL
    }

    /**获取所属分类ID*/
    public Long getCategoryId() {
        return categoryId;  // 返回分类ID
    }

    /**设置所属分类ID*/
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;  // 设置分类ID
    }

    /**获取标签ID列表*/
    public String getTagIds() {
        return tagIds;  // 返回标签ID列表
    }

    /**设置标签ID列表*/
    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;  // 设置标签ID列表
    }

    /**获取文章内容*/
    public String getContent() {
        return content;  // 返回文章内容
    }

    /**设置文章内容*/
    public void setContent(String content) {
        this.content = content;  // 设置文章内容
    }

    /**获取发布状态*/
    public Integer getPublishStatus() {
        return publishStatus;  // 返回发布状态
    }

    /**设置发布状态*/
    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;  // 设置发布状态
    }

    /**获取发布时间*/
    public Date getPublishAt() {
        return publishAt;  // 返回发布时间
    }

    /**设置发布时间*/
    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;  // 设置发布时间
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
}  // TrainingArticle类结束







