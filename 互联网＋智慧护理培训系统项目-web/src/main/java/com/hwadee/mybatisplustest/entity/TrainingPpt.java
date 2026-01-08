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
 * 培训PPT实体类
 * 
 * 功能：存储培训PPT的信息，包括PPT文件URL、页数、分类、标签等
 * 对应数据库表：training_ppt
 */
@TableName("training_ppt")  // 指定对应的数据库表名
public class TrainingPpt {

    @TableId(type = IdType.AUTO)  // 主键，自增长
    private Long id;  // PPT ID

    private String title;  // PPT标题

    private String coverUrl;  // 封面图片URL

    private String fileUrl;  // PPT文件URL

    private Integer pages;  // PPT页数

    private Long categoryId;  // 所属分类ID

    private String tagIds;  // 标签ID列表（逗号分隔）

    private Integer publishStatus;  // 发布状态：0-草稿 1-已发布

    private Date publishAt;  // 发布时间

    private Date createdAt;  // 创建时间

    private Date updatedAt;  // 更新时间

    /**获取PPT ID*/
    public Long getId() {
        return id;  // 返回PPT ID
    }

    /**设置PPT ID*/
    public void setId(Long id) {
        this.id = id;  // 设置PPT ID
    }

    /**获取PPT标题*/
    public String getTitle() {
        return title;  // 返回PPT标题
    }

    /**设置PPT标题*/
    public void setTitle(String title) {
        this.title = title;  // 设置PPT标题
    }

    /**获取封面图片URL*/
    public String getCoverUrl() {
        return coverUrl;  // 返回封面图片URL
    }

    /**设置封面图片URL*/
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;  // 设置封面图片URL
    }

    /**获取PPT文件URL*/
    public String getFileUrl() {
        return fileUrl;  // 返回PPT文件URL
    }

    /**设置PPT文件URL*/
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;  // 设置PPT文件URL
    }

    /**获取PPT页数*/
    public Integer getPages() {
        return pages;  // 返回PPT页数
    }

    /**设置PPT页数*/
    public void setPages(Integer pages) {
        this.pages = pages;  // 设置PPT页数
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
}  // TrainingPpt类结束







