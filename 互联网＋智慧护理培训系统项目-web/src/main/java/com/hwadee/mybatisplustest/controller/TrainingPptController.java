// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入培训PPT实体类
import com.hwadee.mybatisplustest.entity.TrainingPpt;
// 导入培训PPT服务接口
import com.hwadee.mybatisplustest.service.TrainingPptService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

/**
 * 培训PPT管理控制器
 * 
 * 功能：CRUD、搜索、分页、只显示已发布PPT
 * 智慧护理培训系统 - 培训PPT模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/training/ppt", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class TrainingPptController {

    @Resource  // 依赖注入
    private TrainingPptService trainingPptService;  // PPT服务层

    /**创建PPT*/
    @PostMapping("/create")
    public CommonResult<TrainingPpt> create(@RequestBody TrainingPpt ppt) {  // 接收JSON数据
        ppt.setId(null);  // 确保创建操作
        trainingPptService.save(ppt);  // 保存
        return CommonResult.success(ppt);  // 返回结果
    }

    /**更新PPT*/
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody TrainingPpt ppt) {  // 接收JSON
        boolean ok = trainingPptService.updateById(ppt);  // 执行更新
        return ok ? CommonResult.success(true) : CommonResult.error("更新失败");  // 返回结果
    }

    /**删除PPT*/
    @GetMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // URL路径参数
        boolean ok = trainingPptService.removeById(id);  // 执行删除
        return ok ? CommonResult.success(true) : CommonResult.error("删除失败");  // 返回结果
    }

    /**获取PPT详情*/
    @GetMapping("/get/{id}")
    public CommonResult<TrainingPpt> get(@PathVariable("id") Long id) {  // URL路径参数
        TrainingPpt one = trainingPptService.getById(id);  // 查询
        return one != null ? CommonResult.success(one) : CommonResult.error("未找到");  // 返回结果
    }

    /**获取PPT列表-支持搜索和分类筛选，只返回已发布*/
    @GetMapping("/list")
    public CommonResult<?> list(
            @RequestParam(value = "keyword", required = false) String keyword,      // 搜索关键词
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID
        LambdaQueryWrapper<TrainingPpt> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (keyword != null && !keyword.isEmpty()) {  // 关键词搜索
            wrapper.like(TrainingPpt::getTitle, keyword);  // 标题模糊匹配
        }
        if (categoryId != null) {  // 分类筛选
            wrapper.eq(TrainingPpt::getCategoryId, categoryId);  // 分类ID匹配
        }
        wrapper.eq(TrainingPpt::getPublishStatus, 1);  // 只返回已发布的
        wrapper.orderByDesc(TrainingPpt::getPublishAt).orderByDesc(TrainingPpt::getId);  // 按发布时间降序
        return CommonResult.success(trainingPptService.list(wrapper));  // 返回列表
    }

    /**分页查询PPT-支持搜索和分类筛选*/
    @GetMapping("/page")
    public CommonResult<Page<TrainingPpt>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量
            @RequestParam(value = "keyword", required = false) String keyword,    // 搜索关键词
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID
        Page<TrainingPpt> page = new Page<>(pageNo, pageSize);  // 创建分页对象
        LambdaQueryWrapper<TrainingPpt> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (keyword != null && !keyword.isEmpty()) {  // 关键词搜索
            wrapper.like(TrainingPpt::getTitle, keyword);  // 标题模糊匹配
        }
        if (categoryId != null) {  // 分类筛选
            wrapper.eq(TrainingPpt::getCategoryId, categoryId);  // 分类ID匹配
        }
        wrapper.orderByDesc(TrainingPpt::getPublishAt).orderByDesc(TrainingPpt::getId);  // 按发布时间降序
        trainingPptService.page(page, wrapper);  // 执行分页查询
        return CommonResult.success(page);  // 返回分页结果
    }
}  // TrainingPptController类结束




