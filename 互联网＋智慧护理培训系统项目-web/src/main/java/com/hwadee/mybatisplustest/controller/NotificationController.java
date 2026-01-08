// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入通知实体类
import com.hwadee.mybatisplustest.entity.Notification;
// 导入通知服务接口
import com.hwadee.mybatisplustest.service.NotificationService;
// 导入Jakarta EE的Resource注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入Spring Web的注解
import org.springframework.web.bind.annotation.*;

// 导入Java 8时间API的LocalDateTime类
import java.time.LocalDateTime;
// 导入HashMap用于构建响应数据
import java.util.HashMap;
// 导入List集合类
import java.util.List;
// 导入Map接口
import java.util.Map;

/**
 * 通知管理控制器
 * 
 * 功能说明：
 * 1. 提供通知的创建、查询、删除功能
 * 2. 支持按用户查询通知列表
 * 3. 支持标记单个/全部通知为已读
 * 4. 支持筛选未读通知
 * 5. 统计未读通知数量
 * 
 * 使用场景：
 * - 系统通知（新文章、任务提醒等）
 * - 用户消息提醒
 * - 通知已读/未读状态管理
 * 
 * 智慧护理培训系统 - 通知管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/notification
@RequestMapping(value = "/notification", produces = "application/json")
// @CrossOrigin：允许跨域请求
@CrossOrigin(origins = "*")
public class NotificationController {

    /**
     * 通知服务层依赖注入
     */
    @Resource
    private NotificationService notificationService;  // 通知服务层接口

    /**
     * 创建新通知
     * 
     * 功能说明：
     * 1. 创建新通知记录
     * 2. 自动设置为未读状态
     * 3. 自动设置创建时间为当前时间
     * 
     * @param notification 通知对象
     * @return CommonResult 创建结果
     */
    // 创建通知
    @PostMapping("/create")
    public CommonResult<?> createNotification(@RequestBody Notification notification) {  // 接收JSON格式的通知数据
        notification.setId(null);  // 确保是创建操作
        notification.setIsRead(false);  // 设置为未读状态
        notification.setCreatedAt(LocalDateTime.now());  // 设置创建时间为当前时间
        notificationService.save(notification);  // 保存到数据库
        return CommonResult.success(notification);  // 返回成功响应
    }

    /**
     * 获取用户通知列表
     * 
     * 功能说明：
     * 1. 根据用户ID查询通知列表
     * 2. 支持只显示未读通知
     * 3. 按创建时间降序排列
     * 4. 返回通知列表、未读数量、总数量
     * 
     * @param userId 用户ID
     * @param unreadOnly 是否只显示未读（可选）
     * @return CommonResult 包含通知列表和统计信息
     */
    // 获取用户通知列表
    @GetMapping("/user/{userId}")
    public CommonResult<?> getUserNotifications(
            @PathVariable Long userId,  // 从URL路径获取用户ID
            @RequestParam(required = false) Boolean unreadOnly) {  // 是否只显示未读
        // 构建查询条件
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);  // 等于指定用户ID
        
        // 如果只显示未读
        if (unreadOnly != null && unreadOnly) {  // 参数为true时
            wrapper.eq(Notification::getIsRead, false);  // 添加未读条件
        }
        
        wrapper.orderByDesc(Notification::getCreatedAt);  // 按创建时间降序排列
        List<Notification> notifications = notificationService.list(wrapper);  // 执行查询

        // 统计未读数量
        long unreadCount = notificationService.lambdaQuery()
                .eq(Notification::getUserId, userId)  // 用户ID匹配
                .eq(Notification::getIsRead, false)   // 未读状态
                .count();  // 统计数量

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();  // 创建Map对象
        result.put("notifications", notifications);  // 通知列表
        result.put("unreadCount", unreadCount);      // 未读数量
        result.put("totalCount", notifications.size());  // 总数量

        return CommonResult.success(result);  // 返回成功响应
    }

    /**
     * 标记通知为已读
     * 
     * 功能说明：
     * 1. 根据ID标记单个通知为已读
     * 2. 设置阅读时间为当前时间
     * 
     * @param id 通知ID
     * @return CommonResult 操作结果
     */
    // 标记通知为已读
    @PostMapping("/read/{id}")
    public CommonResult<?> markAsRead(@PathVariable Long id) {  // 从URL路径获取通知ID
        Notification notification = notificationService.getById(id);  // 查询通知
        if (notification == null) {  // 如果通知不存在
            return CommonResult.error("通知不存在");  // 返回错误
        }

        notification.setIsRead(true);  // 设置为已读状态
        notification.setReadAt(LocalDateTime.now());  // 设置阅读时间为当前时间
        notificationService.updateById(notification);  // 更新到数据库

        return CommonResult.success("已标记为已读");  // 返回成功响应
    }

    /**
     * 标记所有通知为已读
     * 
     * 功能说明：
     * 1. 根据用户ID标记所有未读通知为已读
     * 2. 批量更新阅读状态和阅读时间
     * 
     * @param userId 用户ID
     * @return CommonResult 操作结果
     */
    // 标记所有通知为已读
    @PostMapping("/read-all/{userId}")
    public CommonResult<?> markAllAsRead(@PathVariable Long userId) {  // 从URL路径获取用户ID
        // 构建查询条件：查找该用户的所有未读通知
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)    // 用户ID匹配
               .eq(Notification::getIsRead, false);     // 未读状态

        List<Notification> unread = notificationService.list(wrapper);  // 查询所有未读通知
        LocalDateTime now = LocalDateTime.now();  // 获取当前时间

        // 遍历所有未读通知，逐个标记为已读
        for (Notification notification : unread) {  // 循环处理每条通知
            notification.setIsRead(true);  // 设置为已读
            notification.setReadAt(now);   // 设置阅读时间
            notificationService.updateById(notification);  // 更新到数据库
        }

        return CommonResult.success("已标记全部为已读");  // 返回成功响应
    }

    /**
     * 删除通知
     * 
     * 功能说明：
     * 1. 根据ID删除通知记录
     * 2. 物理删除，不可恢复
     * 
     * @param id 通知ID
     * @return CommonResult 删除结果
     */
    // 删除通知
    @DeleteMapping("/delete/{id}")
    public CommonResult<?> deleteNotification(@PathVariable Long id) {  // 从URL路径获取通知ID
        boolean removed = notificationService.removeById(id);  // 执行删除操作
        return removed   // 判断删除是否成功
                ? CommonResult.success("删除成功")    // 成功响应
                : CommonResult.error("删除失败");  // 失败响应
    }

    /**
     * 获取未读通知数量
     * 
     * 功能说明：
     * 1. 根据用户ID统计未读通知数量
     * 2. 用于页面头部通知小红点显示
     * 
     * @param userId 用户ID
     * @return CommonResult 包含未读数量
     */
    // 获取未读通知数量
    @GetMapping("/unread-count/{userId}")
    public CommonResult<?> getUnreadCount(@PathVariable Long userId) {  // 从URL路径获取用户ID
        // 使用Lambda查询统计未读数量
        long count = notificationService.lambdaQuery()
                .eq(Notification::getUserId, userId)  // 用户ID匹配
                .eq(Notification::getIsRead, false)   // 未读状态
                .count();  // 统计数量

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("unreadCount", count);  // 放入未读数量

        return CommonResult.success(result);  // 返回成功响应
    }
}  // NotificationController类结束







