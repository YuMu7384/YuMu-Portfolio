// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入培训标签实体类
import com.hwadee.mybatisplustest.entity.TrainingTag;
// 导入培训标签服务接口
import com.hwadee.mybatisplustest.service.TrainingTagService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

/**
 * 培训标签管理控制器
 * 
 * 功能：CRUD、搜索、分页、按状态筛选
 * 智慧护理培训系统 - 培训标签模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/training/tag", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class TrainingTagController {

    @Resource  // 依赖注入
    private TrainingTagService trainingTagService;  // 标签服务层

    /**创建标签*/
    @PostMapping("/create")
    public CommonResult<TrainingTag> create(@RequestBody TrainingTag tag) {  // 接收JSON数据
        tag.setId(null);  // 确保创建操作
        trainingTagService.save(tag);  // 保存
        return CommonResult.success(tag);  // 返回结果
    }

    /**更新标签*/
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody TrainingTag tag) {  // 接收JSON
        boolean ok = trainingTagService.updateById(tag);  // 执行更新
        return ok ? CommonResult.success(true) : CommonResult.error("更新失败");  // 返回结果
    }

    /**删除标签*/
    @GetMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // URL路径参数
        boolean ok = trainingTagService.removeById(id);  // 执行删除
        return ok ? CommonResult.success(true) : CommonResult.error("删除失败");  // 返回结果
    }

    /**获取标签详情*/
    @GetMapping("/get/{id}")
    public CommonResult<TrainingTag> get(@PathVariable("id") Long id) {  // URL路径参数
        TrainingTag one = trainingTagService.getById(id);  // 查询
        return one != null ? CommonResult.success(one) : CommonResult.error("未找到");  // 返回结果
    }

    /**获取标签列表-支持按状态筛选*/
    @GetMapping("/list")
    public CommonResult<Object> list(@RequestParam(value = "status", required = false) Integer status) {  // 状态参数
        LambdaQueryWrapper<TrainingTag> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (status != null) {  // 如果状态不为空
            wrapper.eq(TrainingTag::getStatus, status);  // 添加状态筛选
        }
        wrapper.orderByDesc(TrainingTag::getId);  // 按ID降序
        return CommonResult.success(trainingTagService.list(wrapper));  // 返回列表
    }

    /**分页查询标签-支持按名称搜索*/
    @GetMapping("/page")
    public CommonResult<Page<TrainingTag>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量
            @RequestParam(value = "keyword", required = false) String keyword) {  // 搜索关键词
        Page<TrainingTag> page = new Page<>(pageNo, pageSize);  // 创建分页对象
        LambdaQueryWrapper<TrainingTag> wrapper = new LambdaQueryWrapper<>();  // 构建查询条件
        if (keyword != null && !keyword.isEmpty()) {  // 关键词搜索
            wrapper.like(TrainingTag::getName, keyword);  // 名称模糊匹配
        }
        wrapper.orderByDesc(TrainingTag::getId);  // 按ID降序
        trainingTagService.page(page, wrapper);  // 执行分页查询
        return CommonResult.success(page);  // 返回分页结果
    }
}  // TrainingTagController类结束




