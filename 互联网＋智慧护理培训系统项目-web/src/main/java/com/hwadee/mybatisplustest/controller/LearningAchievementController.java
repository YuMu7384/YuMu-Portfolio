// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入学习成就实体类
import com.hwadee.mybatisplustest.entity.LearningAchievement;
// 导入学习成就服务接口
import com.hwadee.mybatisplustest.service.LearningAchievementService;
// 导入Jakarta EE的Resource注解
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

// 导入HashMap用于构建响应数据
import java.util.HashMap;
// 导入List接口
import java.util.List;
// 导入Map接口
import java.util.Map;

/**
 * 学习成就管理控制器
 * 
 * 功能：获取用户成就、检查并解锁成就、获取成就定义
 * 智慧护理培训系统 - 学习成就系统模块
 */
@RestController  // RESTful控制器
@RequestMapping(value = "/achievement", produces = "application/json")  // 路径映射
@CrossOrigin(origins = "*")  // 允许跨域
public class LearningAchievementController {

    @Resource  // 依赖注入
    private LearningAchievementService achievementService;  // 成就服务层

    /**获取用户成就列表-包含总积分和成就数量*/
    // 获取用户成就列表
    @GetMapping("/user/{userId}")
    public CommonResult<?> getUserAchievements(@PathVariable Long userId) {  // URL路径参数
        // 构建查询条件
        LambdaQueryWrapper<LearningAchievement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningAchievement::getUserId, userId);  // 用户ID匹配
        wrapper.orderByDesc(LearningAchievement::getUnlockedAt);  // 按解锁时间降序
        List<LearningAchievement> achievements = achievementService.list(wrapper);  // 执行查询

        // 计算总积分
        int totalPoints = achievements.stream()  // 转为Stream流
                .mapToInt(a -> a.getPoints() == null ? 0 : a.getPoints())  // 提取积分
                .sum();  // 求和

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("achievements", achievements);  // 成就列表
        result.put("totalPoints", totalPoints);  // 总积分
        result.put("totalCount", achievements.size());  // 成就总数

        return CommonResult.success(result);  // 返回成功响应
    }

    /**检查并自动解锁成就*/
    // 检查并解锁成就
    @PostMapping("/check/{userId}")
    public CommonResult<?> checkAchievements(@PathVariable Long userId) {  // URL路径参数
        achievementService.checkAndUnlockAchievements(userId);  // 调用服务层检查成就
        return CommonResult.success("成就检查完成");  // 返回成功响应
    }

    /**获取所有成就定义-用于显示未解锁的成就*/
    // 获取所有成就定义（用于显示未解锁的成就）
    @GetMapping("/definitions")
    public CommonResult<?> getAchievementDefinitions() {  // 无参数
        // 定义成就列表（可配置化到数据库）
        List<Map<String, Object>> definitions = List.of(
            Map.of(  // 第一个成就：首次完成
                "type", "first_article",  // 成就类型
                "name", "首次完成",  // 成就名称
                "desc", "完成了第一篇文章的学习",  // 成就描述
                "icon", "🎉",  // 成就图标
                "points", 10,  // 获得积分
                "requirement", "完成1篇文章"  // 解锁条件
            ),
            Map.of(  // 第二个成就：半程达成
                "type", "halfway",
                "name", "半程达成",
                "desc", "完成了5篇文章的学习",
                "icon", "📚",
                "points", 30,
                "requirement", "完成5篇文章"
            ),
            Map.of(  // 第三个成就：完美完成
                "type", "completed_all",
                "name", "完美完成",
                "desc", "完成了所有10篇文章的学习",
                "icon", "🏆",
                "points", 100,
                "requirement", "完成10篇文章"
            )
        );

        return CommonResult.success(definitions);  // 返回成就定义列表
    }
}  // LearningAchievementController类结束







