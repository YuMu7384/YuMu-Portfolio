// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入用户收藏实体类
import com.hwadee.mybatisplustest.entity.UserFavorite;
// 导入用户收藏服务接口
import com.hwadee.mybatisplustest.service.UserFavoriteService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

// 导入Java 8时间API的LocalDateTime类
import java.time.LocalDateTime;
// 导入HashMap用于构建响应数据
import java.util.HashMap;
// 导入List接口
import java.util.List;
// 导入Map接口
import java.util.Map;

/**
 * 用户收藏管理控制器
 * 
 * 功能：添加收藏、取消收藏、检查收藏状态、获取收藏列表、统计收藏数量
 * 智慧护理培训系统 - 用户收藏模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/favorite", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class UserFavoriteController {

    @Resource  // 依赖注入
    private UserFavoriteService favoriteService;  // 收藏服务层

    /**添加收藏-支持文章和视频收藏*/
    // 添加收藏
    @PostMapping("/add")
    public CommonResult<?> addFavorite(@RequestBody Map<String, Object> body) {  // 接收JSON数据
        // 从请求体中提取参数
        Long userId = Long.valueOf(body.get("userId").toString());  // 用户ID（必填）
        Long articleId = body.get("articleId") != null ? Long.valueOf(body.get("articleId").toString()) : null;  // 文章ID（可选）
        Long videoId = body.get("videoId") != null ? Long.valueOf(body.get("videoId").toString()) : null;  // 视频ID（可选）

        // 参数校验：文章ID和视频ID至少需要一个
        if (articleId == null && videoId == null) {
            return CommonResult.error("文章ID或视频ID至少需要一个");  // 返回错误
        }

        // 检查是否已经收藏
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);  // 用户ID匹配
        if (articleId != null) {  // 如果是文章收藏
            wrapper.eq(UserFavorite::getArticleId, articleId);  // 文章ID匹配
        }
        if (videoId != null) {  // 如果是视频收藏
            wrapper.eq(UserFavorite::getVideoId, videoId);  // 视频ID匹配
        }

        UserFavorite exist = favoriteService.getOne(wrapper);  // 查询现有收藏
        if (exist != null) {  // 如果已经收藏
            return CommonResult.error("已经收藏过了");  // 返回错误
        }

        // 创建新收藏记录
        UserFavorite favorite = new UserFavorite();  // 实例化收藏对象
        favorite.setUserId(userId);  // 设置用户ID
        favorite.setArticleId(articleId);  // 设置文章ID
        favorite.setVideoId(videoId);  // 设置视频ID
        favorite.setCreatedAt(LocalDateTime.now());  // 设置创建时间
        favoriteService.save(favorite);  // 保存到数据库

        return CommonResult.success("收藏成功");  // 返回成功响应
    }

    /**取消收藏*/
    // 取消收藏
    @PostMapping("/remove")
    public CommonResult<?> removeFavorite(@RequestBody Map<String, Object> body) {  // 接收JSON数据
        // 从请求体中提取参数
        Long userId = Long.valueOf(body.get("userId").toString());  // 用户ID（必填）
        Long articleId = body.get("articleId") != null ? Long.valueOf(body.get("articleId").toString()) : null;  // 文章ID（可选）
        Long videoId = body.get("videoId") != null ? Long.valueOf(body.get("videoId").toString()) : null;  // 视频ID（可选）

        // 构建删除条件
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);  // 用户ID匹配
        if (articleId != null) {  // 如果是文章收藏
            wrapper.eq(UserFavorite::getArticleId, articleId);  // 文章ID匹配
        }
        if (videoId != null) {  // 如果是视频收藏
            wrapper.eq(UserFavorite::getVideoId, videoId);  // 视频ID匹配
        }

        boolean removed = favoriteService.remove(wrapper);  // 执行删除
        return removed ? CommonResult.success("取消收藏成功") : CommonResult.error("取消收藏失败");  // 返回结果
    }

    /**检查是否收藏*/
    // 检查是否收藏
    @GetMapping("/check")
    public CommonResult<?> checkFavorite(
            @RequestParam Long userId,  // 用户ID（必填）
            @RequestParam(required = false) Long articleId,  // 文章ID（可选）
            @RequestParam(required = false) Long videoId) {  // 视频ID（可选）
        // 构建查询条件
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);  // 用户ID匹配
        if (articleId != null) {  // 如果是文章
            wrapper.eq(UserFavorite::getArticleId, articleId);  // 文章ID匹配
        }
        if (videoId != null) {  // 如果是视频
            wrapper.eq(UserFavorite::getVideoId, videoId);  // 视频ID匹配
        }

        UserFavorite favorite = favoriteService.getOne(wrapper);  // 查询收藏记录
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("isFavorite", favorite != null);  // 是否已收藏
        result.put("favorite", favorite);  // 收藏记录

        return CommonResult.success(result);  // 返回结果
    }

    /**获取用户的收藏列表-支持按类型筛选*/
    // 获取用户的收藏列表
    @GetMapping("/user/{userId}")
    public CommonResult<?> getUserFavorites(
            @PathVariable Long userId,  // 用户ID（URL路径参数）
            @RequestParam(required = false) String type) {  // 类型筛选：article/video（可选）
        // 构建查询条件
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFavorite::getUserId, userId);  // 用户ID匹配
        
        // 按类型筛选
        if ("article".equals(type)) {  // 只查询文章收藏
            wrapper.isNotNull(UserFavorite::getArticleId);  // 文章ID不为空
        } else if ("video".equals(type)) {  // 只查询视频收藏
            wrapper.isNotNull(UserFavorite::getVideoId);  // 视频ID不为空
        }

        wrapper.orderByDesc(UserFavorite::getCreatedAt);  // 按创建时间降序
        List<UserFavorite> list = favoriteService.list(wrapper);  // 执行查询

        return CommonResult.success(list);  // 返回列表
    }

    /**获取收藏数量-统计某文章或视频的收藏数*/
    // 获取收藏数量
    @GetMapping("/count")
    public CommonResult<?> getFavoriteCount(
            @RequestParam(required = false) Long articleId,  // 文章ID（可选）
            @RequestParam(required = false) Long videoId) {  // 视频ID（可选）
        // 构建查询条件
        LambdaQueryWrapper<UserFavorite> wrapper = new LambdaQueryWrapper<>();
        if (articleId != null) {  // 如果是文章
            wrapper.eq(UserFavorite::getArticleId, articleId);  // 文章ID匹配
        }
        if (videoId != null) {  // 如果是视频
            wrapper.eq(UserFavorite::getVideoId, videoId);  // 视频ID匹配
        }

        long count = favoriteService.count(wrapper);  // 统计数量
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);  // 收藏数量

        return CommonResult.success(result);  // 返回结果
    }
}  // UserFavoriteController类结束







