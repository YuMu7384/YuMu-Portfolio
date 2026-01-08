package com.hwadee.mybatisplustest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwadee.mybatisplustest.common.CommonResult;
import com.hwadee.mybatisplustest.entity.User;
import com.hwadee.mybatisplustest.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证控制器 - 用户登录与注册管理
 * 
 * 功能说明:
 * 1. 提供三种用户类型的登录接口: 护士、医生、游客
 * 2. 提供用户注册接口(仅限游客角色)
 * 3. 生成登录Token进行身份验证
 * 4. 支持角色验证,确保用户只能以正确的身份登录
 * 
 * 注解说明:
 * @RestController - 标识这是一个RESTful风格的控制器
 *   作用: 自动将方法返回值转换为JSON格式响应
 *   相当于 @Controller + @ResponseBody
 * 
 * @RequestMapping - 定义控制器的基础URL路径
 *   value="/auth" - 所有接口的URL前缀为/auth
 *   produces="application/json" - 声明响应格式为JSON
 * 
 * @CrossOrigin - 允许跨域请求
 *   origins="*" - 允许所有来源的跨域访问
 *   作用: 解决前后端分离时的跨域问题
 * 
 * 依赖说明:
 * - UserService: 用户业务逻辑服务,处理用户数据的CRUD操作
 * - CommonResult: 统一响应结果封装类
 * 
 * @author AI Assistant
 * @version 1.6.0
 */
@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@CrossOrigin(origins = "*")
public class AuthController {

    /**
     * 用户服务层依赖注入
     * 
     * @Resource注解说明:
     * - 这是JavaEE标准的依赖注入注解
     * - 按名称(name)或类型(type)自动注入Bean
     * - 优先按名称匹配,失败则按类型匹配
     * - 与Spring的@Autowired类似,但更标准
     */
    @Resource
    private UserService userService;

    /**
     * 医护人员通用登录接口
     * 
     * 功能说明:
     * 1. 验证医生或护士的用户名和密码
     * 2. 可选验证用户角色(role参数)
     * 3. 登录成功后生成UUID Token
     * 4. 返回用户基本信息和Token
     * 
     * 请求示例:
     * POST /auth/staff/login
     * Body: {
     *   "username": "nurse01",
     *   "password": "123456",
     *   "role": "nurse"  // 可选: nurse(护士) 或 doctor(医生)
     * }
     * 
     * 响应示例:
     * {
     *   "code": "200",
     *   "message": "success",
     *   "data": {
     *     "token": "550e8400-e29b-41d4-a716-446655440000",
     *     "userId": 1,
     *     "username": "nurse01",
     *     "avatar": "https://...",
     *     "role": "nurse",
     *     "userType": "staff"
     *   }
     * }
     * 
     * @param body 请求体,包含username、password、role
     * @return CommonResult 统一响应结果
     * 
     * 注解说明:
     * @PostMapping - 处理POST请求
     * @RequestBody - 将请求体JSON数据绑定到Map参数
     */
    @PostMapping("/staff/login")
    public CommonResult<Object> staffLogin(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");
        String role = body.getOrDefault("role", ""); // nurse 或 doctor
        
        if (username.isEmpty() || password.isEmpty()) {
            return CommonResult.error("用户名或密码不能为空");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username).eq(User::getPassword, password);
        
        // 如果指定了角色,则验证角色
        if (!role.isEmpty()) {
            wrapper.eq(User::getRole, role);
        }
        
        User user = userService.getOne(wrapper);
        
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("token", UUID.randomUUID().toString());
            resp.put("userId", user.getId());
            resp.put("username", user.getUsername());
            resp.put("avatar", user.getAvatar());
            resp.put("role", user.getRole());
            resp.put("userType", "staff");
            return CommonResult.success(resp);
        }

        return CommonResult.error("用户名或密码错误,或角色不匹配");
    }
    
    /**
     * 护士专用登录接口
     * 
     * 功能说明:
     * 1. 自动设置角色为"nurse"
     * 2. 调用通用医护登录方法staffLogin()
     * 3. 仅允许护士角色用户登录
     * 
     * 请求示例:
     * POST /auth/nurse/login
     * Body: {"username": "nurse01", "password": "123456"}
     * 
     * @param body 请求体,包含username和password
     * @return CommonResult 登录结果
     */
    @PostMapping("/nurse/login")
    public CommonResult<Object> nurseLogin(@RequestBody Map<String, String> body) {
        body.put("role", "nurse");
        return staffLogin(body);
    }
    
    /**
     * 医生专用登录接口
     * 
     * 功能说明:
     * 1. 自动设置角色为"doctor"
     * 2. 调用通用医护登录方法staffLogin()
     * 3. 仅允许医生角色用户登录
     * 
     * 请求示例:
     * POST /auth/doctor/login
     * Body: {"username": "doctor01", "password": "123456"}
     * 
     * @param body 请求体,包含username和password
     * @return CommonResult 登录结果
     */
    @PostMapping("/doctor/login")
    public CommonResult<Object> doctorLogin(@RequestBody Map<String, String> body) {
        body.put("role", "doctor");
        return staffLogin(body);
    }

    /**
     * 用户注册接口 - 仅允许注册游客角色
     * 
     * 功能说明:
     * 1. 验证用户名和密码非空
     * 2. 检查用户名是否已存在
     * 3. 强制将用户角色设置为"guest"(游客)
     * 4. 保存新用户到数据库
     * 
     * 安全说明:
     * - 医生和护士账号不能通过此接口注册
     * - 医护账号需要由管理员在后台创建
     * - 防止恶意用户注册管理员权限账号
     * 
     * 请求示例:
     * POST /auth/register
     * Body: {
     *   "username": "testuser",
     *   "password": "123456",
     *   "email": "test@example.com",
     *   "age": 25
     * }
     * 
     * 响应示例:
     * {
     *   "code": "200",
     *   "data": {
     *     "message": "注册成功",
     *     "username": "testuser",
     *     "role": "guest"
     *   }
     * }
     * 
     * @param user 用户实体对象,包含注册信息
     * @return CommonResult 注册结果
     */
    @PostMapping("/register")
    public CommonResult<Object> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return CommonResult.error("用户名不能为空");
        }
        
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return CommonResult.error("密码不能为空");
        }
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User existUser = userService.getOne(wrapper);
        
        if (existUser != null) {
            return CommonResult.error("用户名已存在，请更换用户名");
        }
        
        // 强制设置为游客角色
        user.setRole("guest");
        
        // 保存新用户
        boolean saved = userService.save(user);
        
        if (saved) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "注册成功");
            resp.put("username", user.getUsername());
            resp.put("role", "guest");
            return CommonResult.success(resp);
        } else {
            return CommonResult.error("注册失败，请稍后重试");
        }
    }
    
    /**
     * 游客登录接口
     * 
     * 功能说明:
     * 1. 验证游客用户的用户名和密码
     * 2. 仅允许role="guest"的用户登录
     * 3. 生成Token并返回用户信息
     * 
     * 与医护登录的区别:
     * - 游客权限较低,不能访问管理功能
     * - 仅能浏览培训资源,不能管理病人
     * 
     * 请求示例:
     * POST /auth/guest/login
     * Body: {"username": "testuser", "password": "123456"}
     * 
     * @param body 请求体,包含username和password
     * @return CommonResult 登录结果
     */
    @PostMapping("/guest/login")
    public CommonResult<Object> guestLogin(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");
        
        if (username.isEmpty() || password.isEmpty()) {
            return CommonResult.error("用户名或密码不能为空");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getPassword, password)
               .eq(User::getRole, "guest");
        
        User user = userService.getOne(wrapper);
        
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("token", UUID.randomUUID().toString());
            resp.put("userId", user.getId());
            resp.put("username", user.getUsername());
            resp.put("avatar", user.getAvatar());
            resp.put("role", user.getRole());
            resp.put("userType", "guest");
            return CommonResult.success(resp);
        }

        return CommonResult.error("用户名或密码错误");
    }
}


