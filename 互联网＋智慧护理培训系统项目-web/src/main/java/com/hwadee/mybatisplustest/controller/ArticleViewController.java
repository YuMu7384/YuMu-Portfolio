// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入文章浏览记录实体类
import com.hwadee.mybatisplustest.entity.ArticleView;
// 导入文章浏览服务接口
import com.hwadee.mybatisplustest.service.ArticleViewService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

// 导入Java 8时间API的LocalDateTime类
import java.time.LocalDateTime;
// 导入HashMap用于构建响应数据
import java.util.HashMap;
// 导入Map接口
import java.util.Map;

/**
 * 文章浏览记录控制器
 * 
 * 功能：记录文章浏览、统计浏览量、查询用户浏览历史
 * 智慧护理培训系统 - 文章浏览统计模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/article/view", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class ArticleViewController {

    @Resource  // 依赖注入
    private ArticleViewService viewService;  // 浏览记录服务层

    /**记录文章浏览-自动累加浏览次数*/
    // 记录文章浏览
    @PostMapping("/record")
    public CommonResult<?> recordView(@RequestBody Map<String, Object> body) {  // 接收JSON数据
        // 从请求体中提取文章ID和用户ID
        Long articleId = Long.valueOf(body.get("articleId").toString());  // 文章ID（必填）
        Long userId = body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : null;  // 用户ID（可选）

        // 构建查询条件：查找是否已有该文章的浏览记录
        LambdaQueryWrapper<ArticleView> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleView::getArticleId, articleId);  // 文章ID匹配
        if (userId != null) {  // 如果有用户ID
            wrapper.eq(ArticleView::getUserId, userId);  // 用户ID匹配
        } else {  // 匿名用户
            wrapper.isNull(ArticleView::getUserId);  // 用户ID为null
        }

        ArticleView view = viewService.getOne(wrapper);  // 查询现有记录
        LocalDateTime now = LocalDateTime.now();  // 获取当前时间

        if (view == null) {  // 如果没有记录，创建新记录
            // 创建新记录
            view = new ArticleView();  // 实例化浏览记录对象
            view.setArticleId(articleId);  // 设置文章ID
            view.setUserId(userId);  // 设置用户ID
            view.setViewCount(1);  // 初始浏览次数为1
            view.setLastViewedAt(now);  // 最后浏览时间
            view.setCreatedAt(now);  // 创建时间
            view.setUpdatedAt(now);  // 更新时间
            viewService.save(view);  // 保存到数据库
        } else {  // 如果已有记录，更新浏览次数
            // 更新现有记录
            view.setViewCount((view.getViewCount() == null ? 0 : view.getViewCount()) + 1);  // 浏览次数+1
            view.setLastViewedAt(now);  // 更新最后浏览时间
            view.setUpdatedAt(now);  // 更新修改时间
            viewService.updateById(view);  // 更新到数据库
        }

        // 获取总浏览量（所有用户的浏览次数总和）
        LambdaQueryWrapper<ArticleView> totalWrapper = new LambdaQueryWrapper<>();
        totalWrapper.eq(ArticleView::getArticleId, articleId);  // 查询该文章的所有浏览记录
        long totalViews = viewService.list(totalWrapper).stream()  // 获取列表并转为Stream
                .mapToInt(v -> v.getViewCount() == null ? 0 : v.getViewCount())  // 提取浏览次数
                .sum();  // 求和

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("view", view);  // 当前浏览记录
        result.put("totalViews", totalViews);  // 总浏览量

        return CommonResult.success(result);  // 返回成功响应
    }

    /**获取文章总浏览量*/
    // 获取文章总浏览量
    @GetMapping("/count/{articleId}")
    public CommonResult<?> getViewCount(@PathVariable Long articleId) {  // URL路径参数
        // 查询该文章的所有浏览记录
        LambdaQueryWrapper<ArticleView> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleView::getArticleId, articleId);  // 文章ID匹配
        
        // 计算总浏览量
        long totalViews = viewService.list(wrapper).stream()  // 获取列表并转为Stream
                .mapToInt(v -> v.getViewCount() == null ? 0 : v.getViewCount())  // 提取浏览次数
                .sum();  // 求和

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("articleId", articleId);  // 文章ID
        result.put("totalViews", totalViews);  // 总浏览量

        return CommonResult.success(result);  // 返回成功响应
    }

    /**获取用户浏览的文章列表-按浏览时间降序*/
    // 获取用户浏览的文章列表
    @GetMapping("/user/{userId}")
    public CommonResult<?> getUserViewedArticles(@PathVariable Long userId) {  // URL路径参数
        // 构建查询条件
        LambdaQueryWrapper<ArticleView> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleView::getUserId, userId);  // 用户ID匹配
        wrapper.orderByDesc(ArticleView::getLastViewedAt);  // 按最后浏览时间降序

        var list = viewService.list(wrapper);  // 执行查询
        return CommonResult.success(list);  // 返回列表
    }
}  // ArticleViewController类结束

