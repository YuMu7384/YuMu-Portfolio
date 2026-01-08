// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入管理员实体类
import com.hwadee.mybatisplustest.entity.Admin;
// 导入管理员服务接口
import com.hwadee.mybatisplustest.service.AdminService;
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
// 导入UUID类，用于生成随机Token
import java.util.UUID;

/**
 * 管理员控制器
 * 
 * 功能说明：
 * 1. 提供管理员登录功能
 * 2. 获取管理员信息
 * 3. 更新管理员资料
 * 4. 验证管理员状态（是否被禁用）
 * 
 * 使用场景：
 * - 管理员后台登录
 * - 管理员信息管理
 * - 管理员账号状态管理
 * 
 * 智慧护理培训系统 - 管理员管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/admin
@RequestMapping(value = "/admin", produces = "application/json")
// @CrossOrigin：允许跨域请求
@CrossOrigin(origins = "*")
public class AdminController {

    /**
     * 管理员服务层依赖注入
     */
    @Resource
    private AdminService adminService;  // 管理员服务层接口

    /**
     * 管理员登录
     * 
     * 功能说明：
     * 1. 验证管理员用户名和密码
     * 2. 检查管理员账号状态（status=1为正常）
     * 3. 登录成功后生成UUID Token
     * 4. 更新最后登录时间
     * 5. 返回管理员基本信息和Token
     * 
     * 请求示例：
     * POST /admin/login
     * Body: {"username": "admin", "password": "123456"}
     * 
     * 响应示例（成功）：
     * {
     *   "code": "200",
     *   "data": {
     *     "token": "uuid-string",
     *     "adminId": 1,
     *     "username": "admin",
     *     "realName": "张三",
     *     "userType": "admin"
     *   }
     * }
     * 
     * @param body 请求体，包含username和password
     * @return CommonResult 登录结果
     */
    @PostMapping("/login")
    public CommonResult<Object> login(@RequestBody Map<String, String> body) {  // 接收Map类型的请求数据
        // 从请求体中获取用户名和密码，默认值为空字符串
        String username = body.getOrDefault("username", "");  // 获取用户名
        String password = body.getOrDefault("password", "");  // 获取密码
        
        // 数据校验：检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {  // 如果任一为空
            return CommonResult.error("用户名或密码不能为空");  // 返回错误信息
        }
        
        // 构建Lambda查询条件
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username)  // 用户名匹配
               .eq(Admin::getPassword, password)  // 密码匹配
               .eq(Admin::getStatus, 1);           // 状态为1（正常）
        Admin admin = adminService.getOne(wrapper);  // 执行查询，获取单个管理员对象
        
        // 判断管理员是否存在
        if (admin != null) {  // 登录成功
            // 更新最后登录时间
            admin.setLastLoginTime(LocalDateTime.now());  // 设置为当前时间
            adminService.updateById(admin);  // 更新到数据库
            
            // 构建响应数据
            Map<String, Object> resp = new HashMap<>();  // 创建HashMap存储响应数据
            resp.put("token", UUID.randomUUID().toString());  // 生成随机UUID作为Token
            resp.put("adminId", admin.getId());         // 管理员ID
            resp.put("username", admin.getUsername());  // 用户名
            resp.put("realName", admin.getRealName());  // 真实姓名
            resp.put("avatar", admin.getAvatar());      // 头像地址
            resp.put("userType", "admin");              // 用户类型标识
            return CommonResult.success(resp);  // 返回成功响应
        }

        // 登录失败，返回错误信息
        return CommonResult.error("用户名或密码错误,或账号已被禁用");
    }

    /**
     * 获取管理员信息
     * 
     * 功能说明：
     * 1. 根据ID获取管理员详细信息
     * 2. 出于安全考虑，不返回密码字段
     * 
     * 请求示例：
     * GET /admin/info/1
     * 
     * 响应示例：
     * {"code": "200", "data": {"id": 1, "username": "admin", "password": null, ...}}
     * 
     * @param id 管理员ID
     * @return CommonResult<Admin> 管理员信息（不包含密码）
     */
    @GetMapping("/info/{id}")
    public CommonResult<Admin> getInfo(@PathVariable Long id) {  // 从URL路径获取管理员ID
        Admin admin = adminService.getById(id);  // 根据ID查询管理员
        if (admin != null) {  // 如果管理员存在
            admin.setPassword(null);  // 将密码设为null，不返回给前端（安全考虑）
            return CommonResult.success(admin);  // 返回成功响应
        }
        return CommonResult.error("管理员不存在");  // 返回错误信息
    }

    /**
     * 更新管理员信息
     * 
     * 功能说明：
     * 1. 根据ID更新管理员信息
     * 2. 支持部分字段更新
     * 3. 可用于修改管理员资料、状态等
     * 
     * 请求示例：
     * PUT /admin/update
     * Body: {"id": 1, "realName": "张三", "email": "admin@example.com"}
     * 
     * 响应示例：
     * {"code": "200", "data": true}
     * 
     * @param admin 管理员对象，必须包含id字段
     * @return CommonResult<Boolean> 更新是否成功
     */
    @PutMapping("/update")
    public CommonResult<Boolean> update(@RequestBody Admin admin) {  // 接收JSON格式的管理员数据
        boolean ok = adminService.updateById(admin);  // 执行更新操作
        return ok   // 判断更新是否成功
                ? CommonResult.success(true)    // 成功响应
                : CommonResult.error("更新失败");  // 失败响应
    }
}  // AdminController类结束


