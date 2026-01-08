// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入用户学习进度实体类
import com.hwadee.mybatisplustest.entity.UserLearningProgress;
// 导入学习成就服务接口
import com.hwadee.mybatisplustest.service.LearningAchievementService;
// 导入用户学习进度服务接口
import com.hwadee.mybatisplustest.service.UserLearningProgressService;
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
 * 用户学习进度管理控制器
 * 
 * 功能：记录学习进度、获取进度统计、成就检查
 * 智慧护理培训系统 - 学习进度跟踪模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/learning/progress", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class UserLearningProgressController {

    @Resource  // 依赖注入
    private UserLearningProgressService progressService;  // 学习进度服务层

    @Resource  // 依赖注入
    private LearningAchievementService achievementService;  // 成就服务层

    /**记录学习进度-支持文章和视频学习进度跟踪*/
    // 记录学习进度（学习文章或视频）
    @PostMapping("/record")
    public CommonResult<?> recordProgress(@RequestBody Map<String, Object> body) {  // 接收JSON数据
        // 从请求体中提取参数
        Long userId = Long.valueOf(body.get("userId").toString());  // 用户ID（必填）
        Long articleId = body.get("articleId") != null ? Long.valueOf(body.get("articleId").toString()) : null;  // 文章ID（可选）
        Long videoId = body.get("videoId") != null ? Long.valueOf(body.get("videoId").toString()) : null;  // 视频ID（可选）
        Integer progressPercent = body.get("progressPercent") != null ? Integer.valueOf(body.get("progressPercent").toString()) : 100;  // 进度百分比（默认100）
        Boolean isCompleted = body.get("isCompleted") != null ? Boolean.valueOf(body.get("isCompleted").toString()) : false;  // 是否完成（默认false）

        // 参数校验：文章ID和视频ID至少需要一个
        if (articleId == null && videoId == null) {
            return CommonResult.error("文章ID或视频ID至少需要一个");  // 返回错误
        }

        // 查询现有学习进度记录
        LambdaQueryWrapper<UserLearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLearningProgress::getUserId, userId);  // 用户ID匹配
        if (articleId != null) {  // 如果是文章
            wrapper.eq(UserLearningProgress::getArticleId, articleId);  // 文章ID匹配
        }
        if (videoId != null) {  // 如果是视频
            wrapper.eq(UserLearningProgress::getVideoId, videoId);  // 视频ID匹配
        }

        UserLearningProgress progress = progressService.getOne(wrapper);  // 查询现有记录
        LocalDateTime now = LocalDateTime.now();  // 获取当前时间

        if (progress == null) {  // 如果没有记录，创建新记录
            // 创建新记录
            progress = new UserLearningProgress();  // 实例化进度对象
            progress.setUserId(userId);  // 设置用户ID
            progress.setArticleId(articleId);  // 设置文章ID
            progress.setVideoId(videoId);  // 设置视频ID
            progress.setProgressPercent(progressPercent);  // 设置进度百分比
            progress.setIsCompleted(isCompleted);  // 设置完成状态
            progress.setStartedAt(now);  // 设置开始学习时间
            progress.setLastStudiedAt(now);  // 设置最后学习时间
            progress.setCreatedAt(now);  // 设置创建时间
            if (isCompleted) {  // 如果已完成
                progress.setCompletedAt(now);  // 设置完成时间
            }
            progressService.save(progress);  // 保存到数据库
        } else {  // 如果已有记录，更新进度
            // 更新现有记录
            boolean wasCompleted = Boolean.TRUE.equals(progress.getIsCompleted());  // 记录之前是否已完成
            progress.setProgressPercent(progressPercent);  // 更新进度百分比
            progress.setIsCompleted(isCompleted);  // 更新完成状态
            progress.setLastStudiedAt(now);  // 更新最后学习时间
            progress.setUpdatedAt(now);  // 更新修改时间
            if (isCompleted && progress.getCompletedAt() == null) {  // 如果刚完成且之前未完成
                progress.setCompletedAt(now);  // 设置完成时间
            }
            progressService.updateById(progress);  // 更新到数据库
            
            // 如果刚完成一篇文章，检查成就
            if (isCompleted && !wasCompleted && articleId != null) {  // 刚完成文章学习
                try {  // 异常处理
                    achievementService.checkAndUnlockAchievements(userId);  // 检查并解锁成就
                } catch (Exception e) {  // 捕获异常
                    // 成就检查失败不影响学习进度记录
                    System.err.println("检查成就失败: " + e.getMessage());  // 输出错误日志
                }
            }
        }

        return CommonResult.success(progress);  // 返回进度记录
    }

    /**获取用户学习进度-包含完成统计和总进度*/
    // 获取用户学习进度
    @GetMapping("/user/{userId}")
    public CommonResult<?> getUserProgress(@PathVariable("userId") Long userId) {  // URL路径参数
        // 查询用户所有学习进度记录
        LambdaQueryWrapper<UserLearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLearningProgress::getUserId, userId);  // 用户ID匹配
        List<UserLearningProgress> list = progressService.list(wrapper);  // 执行查询

        // 计算总进度
        long completedArticles = list.stream()  // 转为Stream流
                .filter(p -> p.getArticleId() != null && Boolean.TRUE.equals(p.getIsCompleted()))  // 筛选已完成的文章
                .count();  // 统计数量
        long completedVideos = list.stream()  // 转为Stream流
                .filter(p -> p.getVideoId() != null && Boolean.TRUE.equals(p.getIsCompleted()))  // 筛选已完成的视频
                .count();  // 统计数量

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("progressList", list);  // 进度列表
        result.put("completedArticles", completedArticles);  // 完成的文章数
        result.put("completedVideos", completedVideos);  // 完成的视频数
        result.put("totalProgress", Math.round((double) completedArticles / 10 * 100));  // 总进度百分比（假设共10篇文章）
        
        return CommonResult.success(result);  // 返回成功响应
    }

    /**获取用户学习某篇文章的进度详情*/
    // 获取用户学习某篇文章的进度
    @GetMapping("/user/{userId}/article/{articleId}")
    public CommonResult<?> getUserArticleProgress(
            @PathVariable("userId") Long userId,      // 用户ID（URL路径参数）
            @PathVariable("articleId") Long articleId) {  // 文章ID（URL路径参数）
        // 构建查询条件
        LambdaQueryWrapper<UserLearningProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLearningProgress::getUserId, userId)      // 用户ID匹配
               .eq(UserLearningProgress::getArticleId, articleId);  // 文章ID匹配
        UserLearningProgress progress = progressService.getOne(wrapper);  // 查询进度记录
        
        if (progress == null) {  // 如果没有学习记录
            return CommonResult.success(null);  // 返回null
        }
        
        return CommonResult.success(progress);  // 返回进度记录
    }

    /**管理员：获取所有用户的学习进度统计*/
    // 管理员：获取所有用户的学习进度
    @GetMapping("/admin/all")
    public CommonResult<?> getAllUsersProgress() {  // 无参数
        List<UserLearningProgress> all = progressService.list();  // 查询所有学习进度记录
        
        // 按用户分组统计
        Map<Long, Map<String, Object>> userStats = new HashMap<>();  // 用户统计Map（userId -> stats）
        
        for (UserLearningProgress p : all) {  // 遍历所有进度记录
            Long userId = p.getUserId();  // 获取用户ID
            userStats.putIfAbsent(userId, new HashMap<>());  // 初始化用户统计
            Map<String, Object> stats = userStats.get(userId);  // 获取用户统计对象
            
            // 统计已完成的文章数
            if (p.getArticleId() != null && Boolean.TRUE.equals(p.getIsCompleted())) {  // 文章已完成
                stats.put("completedArticles", 
                    ((Number) stats.getOrDefault("completedArticles", 0)).longValue() + 1);  // 累加文章数
            }
            // 统计已完成的视频数
            if (p.getVideoId() != null && Boolean.TRUE.equals(p.getIsCompleted())) {  // 视频已完成
                stats.put("completedVideos", 
                    ((Number) stats.getOrDefault("completedVideos", 0)).longValue() + 1);  // 累加视频数
            }
        }
        
        // 计算每个用户的总进度
        for (Map<String, Object> stats : userStats.values()) {  // 遍历所有用户统计
            long completedArticles = ((Number) stats.getOrDefault("completedArticles", 0)).longValue();  // 获取已完成文章数
            stats.put("totalProgress", Math.round((double) completedArticles / 10 * 100));  // 计算总进度百分比
        }
        
        return CommonResult.success(userStats);  // 返回用户统计数据
    }
}  // UserLearningProgressController类结束

