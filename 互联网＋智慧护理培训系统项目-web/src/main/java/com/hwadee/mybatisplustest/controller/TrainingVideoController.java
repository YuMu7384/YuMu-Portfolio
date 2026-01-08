// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入培训视频实体类
import com.hwadee.mybatisplustest.entity.TrainingVideo;
// 导入培训视频服务接口
import com.hwadee.mybatisplustest.service.TrainingVideoService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

/**
 * 培训视频管理控制器
 * 
 * 功能：CRUD、搜索、分页、只显示已发布视频
 * 智慧护理培训系统 - 培训视频模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/training/video", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class TrainingVideoController {

    @Resource  // 依赖注入
    private TrainingVideoService trainingVideoService;  // 视频服务层

    /**创建视频*/
    @PostMapping("/create")
    public CommonResult<TrainingVideo> create(@RequestBody TrainingVideo video) {  // 接收JSON数据
        video.setId(null);  // 确保创建操作
        trainingVideoService.save(video);  // 保存
        return CommonResult.success(video);  // 返回结果
    }

    /**更新视频*/
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody TrainingVideo video) {  // 接收JSON
        boolean ok = trainingVideoService.updateById(video);  // 执行更新
        return ok ? CommonResult.success(true) : CommonResult.error("更新失败");  // 返回结果
    }

    /**删除视频*/
    @GetMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // URL路径参数
        boolean ok = trainingVideoService.removeById(id);  // 执行删除
        return ok ? CommonResult.success(true) : CommonResult.error("删除失败");  // 返回结果
    }

    /**获取视频详情*/
    @GetMapping("/get/{id}")
    public CommonResult<TrainingVideo> get(@PathVariable("id") Long id) {  // URL路径参数
        TrainingVideo one = trainingVideoService.getById(id);  // 查询
        return one != null ? CommonResult.success(one) : CommonResult.error("未找到");  // 返回结果
    }

    /**获取视频列表-支持搜索和分类筛选，只返回已发布*/
    @GetMapping("/list")
    public CommonResult<?> list(
            @RequestParam(value = "keyword", required = false) String keyword,      // 搜索关键词
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID
        LambdaQueryWrapper<TrainingVideo> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (keyword != null && !keyword.isEmpty()) {  // 关键词搜索
            wrapper.like(TrainingVideo::getTitle, keyword);  // 标题模糊匹配
        }
        if (categoryId != null) {  // 分类筛选
            wrapper.eq(TrainingVideo::getCategoryId, categoryId);  // 分类ID匹配
        }
        wrapper.eq(TrainingVideo::getPublishStatus, 1);  // 只返回已发布的
        wrapper.orderByDesc(TrainingVideo::getPublishAt).orderByDesc(TrainingVideo::getId);  // 按发布时间降序
        return CommonResult.success(trainingVideoService.list(wrapper));  // 返回列表
    }

    /**分页查询视频-支持搜索和分类筛选*/
    @GetMapping("/page")
    public CommonResult<Page<TrainingVideo>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量
            @RequestParam(value = "keyword", required = false) String keyword,    // 搜索关键词
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID
        Page<TrainingVideo> page = new Page<>(pageNo, pageSize);  // 创建分页对象
        LambdaQueryWrapper<TrainingVideo> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (keyword != null && !keyword.isEmpty()) {  // 关键词搜索
            wrapper.like(TrainingVideo::getTitle, keyword);  // 标题模糊匹配
        }
        if (categoryId != null) {  // 分类筛选
            wrapper.eq(TrainingVideo::getCategoryId, categoryId);  // 分类ID匹配
        }
        wrapper.orderByDesc(TrainingVideo::getPublishAt).orderByDesc(TrainingVideo::getId);  // 按发布时间降序
        trainingVideoService.page(page, wrapper);  // 执行分页查询
        return CommonResult.success(page);  // 返回分页结果
    }
}  // TrainingVideoController类结束




