// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入培训分类实体类
import com.hwadee.mybatisplustest.entity.TrainingCategory;
// 导入培训分类服务接口
import com.hwadee.mybatisplustest.service.TrainingCategoryService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

/**
 * 培训分类管理控制器
 * 
 * 功能说明：
 * 1. 提供培训分类的增删改查(CRUD)功能
 * 2. 支持按状态筛选分类
 * 3. 支持按分类名称搜索
 * 4. 支持按排序号和ID排序
 * 5. 支持分页查询
 * 
 * 使用场景：
 * - 管理培训文章分类
 * - 前端分类导航菜单
 * - 培训资源分类管理
 * 
 * 智慧护理培训系统 - 培训分类管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/training/category
@RequestMapping(value = "/training/category", produces = "application/json")
// @CrossOrigin：允许跨域请求
@CrossOrigin(origins = "*")
public class TrainingCategoryController {

    /**
     * 培训分类服务层依赖注入
     */
    @Resource
    private TrainingCategoryService trainingCategoryService;  // 培训分类服务层接口

    /**
     * 创建新培训分类
     * 
     * @param category 分类对象
     * @return CommonResult<TrainingCategory> 创建结果
     */
    @PostMapping("/create")
    public CommonResult<TrainingCategory> create(@RequestBody TrainingCategory category) {  // 接收JSON格式的分类数据
        category.setId(null);  // 确保是创建操作
        trainingCategoryService.save(category);  // 保存到数据库
        return CommonResult.success(category);  // 返回成功响应
    }

    /**
     * 更新培训分类信息
     * 
     * @param category 分类对象，必须包含id
     * @return CommonResult<Boolean> 更新结果
     */
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody TrainingCategory category) {  // 接收JSON格式的分类数据
        boolean ok = trainingCategoryService.updateById(category);  // 执行更新
        return ok ? CommonResult.success(true) : CommonResult.error("更新失败");  // 返回结果
    }

    /**
     * 删除培训分类
     * 
     * @param id 分类ID
     * @return CommonResult<Boolean> 删除结果
     */
    @GetMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // 从URL路径获取ID
        boolean ok = trainingCategoryService.removeById(id);  // 执行删除
        return ok ? CommonResult.success(true) : CommonResult.error("删除失败");  // 返回结果
    }

    /**
     * 根据ID获取分类详情
     * 
     * @param id 分类ID
     * @return CommonResult<TrainingCategory> 分类详情
     */
    @GetMapping("/get/{id}")
    public CommonResult<TrainingCategory> get(@PathVariable("id") Long id) {  // 从URL路径获取ID
        TrainingCategory one = trainingCategoryService.getById(id);  // 查询分类
        return one != null ? CommonResult.success(one) : CommonResult.error("未找到");  // 返回结果
    }

    /**
     * 获取分类列表 - 不分页
     * 
     * 功能说明：
     * 1. 支持按状态筛选
     * 2. 按排序号升序，再按ID降序
     * 
     * @param status 分类状态（可选）
     * @return CommonResult 分类列表
     */
    @GetMapping("/list")
    public CommonResult<Object> list(@RequestParam(value = "status", required = false) Integer status) {  // 获取状态参数
        // 构建查询条件
        LambdaQueryWrapper<TrainingCategory> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {  // 如果状态不为空
            wrapper.eq(TrainingCategory::getStatus, status);  // 添加状态筛选条件
        }
        wrapper.orderByAsc(TrainingCategory::getSortOrder)  // 按排序号升序
               .orderByDesc(TrainingCategory::getId);         // 再按ID降序
        return CommonResult.success(trainingCategoryService.list(wrapper));  // 返回列表
    }

    /**
     * 分页查询分类列表
     * 
     * 功能说明：
     * 1. 支持分页显示
     * 2. 支持按分类名称搜索
     * 3. 按排序号升序排列
     * 
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键词
     * @return CommonResult<Page<TrainingCategory>> 分页结果
     */
    @GetMapping("/page")
    public CommonResult<Page<TrainingCategory>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码，默认1
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量，默认10
            @RequestParam(value = "keyword", required = false) String keyword) {  // 搜索关键词
        Page<TrainingCategory> page = new Page<>(pageNo, pageSize);  // 创建分页对象
        LambdaQueryWrapper<TrainingCategory> wrapper = new LambdaQueryWrapper<>();  // 创建查询构造器
        if (keyword != null && !keyword.isEmpty()) {  // 如果关键词不为空
            wrapper.like(TrainingCategory::getName, keyword);  // 按名称模糊搜索
        }
        wrapper.orderByAsc(TrainingCategory::getSortOrder)  // 按排序号升序
               .orderByDesc(TrainingCategory::getId);         // 再按ID降序
        trainingCategoryService.page(page, wrapper);  // 执行分页查询
        return CommonResult.success(page);  // 返回分页结果
    }
}  // TrainingCategoryController类结束




