// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入用户实体类
import com.hwadee.mybatisplustest.entity.User;
// 导入用户服务接口
import com.hwadee.mybatisplustest.service.UserService;
// 导入Jakarta EE的Resource注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入Spring Web的注解：RestController、RequestMapping、PathVariable、RequestBody等
import org.springframework.web.bind.annotation.*;

// 导入Java的List集合类
import java.util.List;

/**
 * 用户管理控制器
 * 
 * 功能说明：
 * 1. 提供用户信息的增删改查(CRUD)接口
 * 2. 支持分页查询和关键词搜索
 * 3. 处理用户数据的校验和更新
 * 
 * 技术说明：
 * - @RestController：标识这是一个RESTful风格的控制器，自动将返回值转为JSON
 * - @RequestMapping：定义统一的请求路径前缀/user和响应类型application/json
 * - @CrossOrigin：允许跨域请求，解决前后端分离的跨域问题
 * 
 * 智慧护理培训系统 - 用户管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解 = @Controller + @ResponseBody，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/user，响应类型为application/json
@RequestMapping(value = "/user", produces = "application/json")
// @CrossOrigin：允许所有来源的跨域请求，生产环境应限制具体域名
@CrossOrigin(origins = "*")
public class UserController {

    /**
     * 用户服务层依赖注入
     * @Resource：JavaEE标准注解，按名称或类型自动注入UserService实例
     * 作用：通过Spring容器自动装配UserService，用于处理业务逻辑
     */
    @Resource
    private UserService userService;  // 用户服务层接口，处理用户相关的业务逻辑

    /**
     * 创建新用户接口
     * 
     * 功能说明：
     * 1. 接收前端传来的用户信息（JSON格式）
     * 2. 验证用户名是否为空
     * 3. 调用Service层保存用户到数据库
     * 4. 返回创建成功的用户对象
     * 
     * 请求方式：POST
     * 请求路径：/user/create
     * 请求体格式：application/json
     * 
     * 请求示例：
     * POST /user/create
     * Body: {"username": "nurse01", "password": "123456", "role": "nurse"}
     * 
     * 响应示例：
     * {"code": "200", "message": "success", "data": {...}}
     * 
     * @param body 用户对象，由@RequestBody将JSON自动转换为User实体
     * @return CommonResult<User> 统一响应结果，包含创建的用户信息
     */
    // @PostMapping：处理POST请求，consumes指定接受application/json格式的请求体
    @PostMapping(value = "/create", consumes = "application/json")
    public CommonResult<User> create(@RequestBody User body){  // @RequestBody：将请求体JSON转换为User对象
        // 数据校验：检查用户名是否为空或空字符串
        if (body.getUsername() == null || body.getUsername().isEmpty()){
            // 返回错误结果，提示用户名不能为空
            return CommonResult.error("username 不能为空");
        }
        // 调用Service层的save方法，将用户信息保存到数据库
        userService.save(body);  // MyBatis-Plus提供的save方法，自动生成INSERT语句
        // 返回成功结果，包含保存后的用户对象（含自动生成的ID）
        return CommonResult.success(body);
    }

    /**
     * 获取所有用户列表接口（不分页）
     * 
     * 功能说明：
     * 1. 查询数据库中所有用户记录
     * 2. 返回完整的用户列表
     * 3. 不进行分页，适合数据量较小的场景
     * 
     * 请求方式：GET
     * 请求路径：/user/list
     * 
     * 请求示例：
     * GET /user/list
     * 
     * 响应示例：
     * {"code": "200", "data": [{...}, {...}]}
     * 
     * 注意事项：
     * - 数据量大时建议使用/page接口进行分页查询
     * - 返回的用户列表包含所有字段（包括密码，实际应用中应过滤敏感字段）
     * 
     * @return CommonResult<List<User>> 包含用户列表的统一响应结果
     */
    // @GetMapping：处理GET请求，映射到/user/list路径
    @GetMapping("/list")
    public CommonResult<List<User>> list() {  // 返回类型为List<User>的统一结果
        // 调用Service层的list()方法，查询所有用户记录
        List<User> list = userService.list();  // MyBatis-Plus提供的list()方法，执行SELECT * FROM user
        // 将查询结果封装到CommonResult中返回
        return CommonResult.success(list);  // success方法将list包装为成功响应
    }

    /**
     * 分页查询用户接口（支持关键词搜索）
     * 
     * 功能说明：
     * 1. 支持分页查询用户列表
     * 2. 支持按用户名或邮箱模糊搜索
     * 3. 返回分页对象，包含总数、当前页数据等信息
     * 
     * 请求方式：GET
     * 请求路径：/user/page
     * 
     * 请求参数：
     * - pageNo：页码，默认1
     * - pageSize：每页数量，默认10
     * - keyword：搜索关键词（可选），搜索用户名或邮箱
     * 
     * 请求示例：
     * GET /user/page?pageNo=1&pageSize=10&keyword=张三
     * 
     * 响应示例：
     * {"code": "200", "data": {"records": [...], "total": 50, "current": 1}}
     * 
     * @param pageNo 页码，@RequestParam自动从URL参数获取，默认值为1
     * @param pageSize 每页数量，默认值为10
     * @param keyword 搜索关键词，可选参数（required=false）
     * @return CommonResult<Page<User>> 包含分页数据的统一响应结果
     */
    // @GetMapping：处理GET请求，映射到/user/page路径
    @GetMapping("/page")
    public CommonResult<Page<User>> page(
            @RequestParam(defaultValue = "1") Integer pageNo,      // @RequestParam：从URL获取参数pageNo
            @RequestParam(defaultValue = "10") Integer pageSize,   // 每页记录数，默认10条
            @RequestParam(required = false) String keyword) {      // 可选参数keyword，用于搜索
        // 创建分页对象，传入当前页码和每页数量
        Page<User> page = new Page<>(pageNo, pageSize);  // MyBatis-Plus的Page对象
        
        // 判断是否有搜索关键词
        if (keyword != null && !keyword.isEmpty()) {  // 关键词不为空时执行搜索
            // 使用Lambda查询构造器进行模糊搜索
            page = userService.lambdaQuery()              // 开始Lambda查询
                    .like(User::getUsername, keyword)     // 用户名模糊匹配：WHERE username LIKE '%keyword%'
                    .or()                                  // OR条件连接
                    .like(User::getEmail, keyword)        // 或邮箱模糊匹配：OR email LIKE '%keyword%'
                    .page(page);                           // 执行分页查询
        } else {  // 没有关键词时，查询所有数据
            // 直接分页查询，不加任何条件
            page = userService.page(page);  // 执行：SELECT * FROM user LIMIT pageSize OFFSET (pageNo-1)*pageSize
        }
        // 返回分页结果，包含records(数据列表)、total(总记录数)、current(当前页)等信息
        return CommonResult.success(page);
    }

    /**
     * 根据ID获取单个用户详情接口
     * 
     * 功能说明：
     * 1. 根据用户ID查询用户详细信息
     * 2. 验证用户是否存在
     * 3. 返回用户完整信息
     * 
     * 请求方式：GET
     * 请求路径：/user/get/{id}
     * 
     * 请求示例：
     * GET /user/get/1
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": {"id": 1, "username": "admin", ...}}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "用户不存在"}
     * 
     * @param id 用户ID，@PathVariable从URL路径中获取，如/user/get/1中的1
     * @return CommonResult<User> 包含用户信息的统一响应结果
     */
    // @GetMapping：处理GET请求，{id}为路径变量占位符
    @GetMapping("/get/{id}")
    public CommonResult<User> get(@PathVariable Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的getById方法，根据主键ID查询用户
        User user = userService.getById(id);  // 执行：SELECT * FROM user WHERE id = #{id}
        
        // 判断用户是否存在
        if (user == null) {  // 如果查询结果为null，说明用户不存在
            // 返回错误结果，提示用户不存在
            return CommonResult.error("用户不存在");
        }
        // 用户存在，返回成功结果和用户数据
        return CommonResult.success(user);
    }

    /**
     * 更新用户信息接口
     * 
     * 功能说明：
     * 1. 根据用户ID更新用户信息
     * 2. 密码为空时保留原密码（不更新密码）
     * 3. 支持部分字段更新
     * 
     * 请求方式：PUT
     * 请求路径：/user/update
     * 请求体格式：application/json
     * 
     * 请求示例：
     * PUT /user/update
     * Body: {"id": 1, "username": "newname", "age": 30}
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": {...}}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "用户ID不能为空"}
     * 
     * 业务逻辑：
     * - 如果密码字段为空，则保留数据库中的原密码
     * - 其他字段正常更新
     * 
     * @param user 用户对象，包含要更新的字段和用户ID
     * @return CommonResult<User> 包含更新后用户信息的统一响应结果
     */
    // @PutMapping：处理PUT请求，RESTful规范中PUT用于更新资源
    @PutMapping("/update")
    public CommonResult<User> update(@RequestBody User user) {  // @RequestBody：将JSON请求体转换为User对象
        // 数据校验：检查用户ID是否为空
        if (user.getId() == null) {  // ID为null表示无法定位要更新的用户
            // 返回错误结果，提示ID不能为空
            return CommonResult.error("用户ID不能为空");
        }
        
        // 密码处理逻辑：如果前端传来的密码为空，则保留原密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {  // 密码为空或空字符串
            // 查询数据库中的原用户信息
            User existUser = userService.getById(user.getId());  // 获取数据库中的原用户对象
            if (existUser != null) {  // 如果原用户存在
                // 将原密码设置回user对象，避免密码被清空
                user.setPassword(existUser.getPassword());  // 保留原密码
            }
        }
        
        // 调用Service层的updateById方法更新用户信息
        boolean result = userService.updateById(user);  // 执行：UPDATE user SET ... WHERE id = #{id}
        
        // 根据更新结果返回不同的响应
        if (result) {  // 更新成功（影响行数>0）
            // 返回成功结果和更新后的用户对象
            return CommonResult.success(user);
        } else {  // 更新失败（可能ID不存在或数据库异常）
            // 返回错误结果
            return CommonResult.error("更新失败");
        }
    }

    /**
     * 删除用户接口
     * 
     * 功能说明：
     * 1. 根据用户ID删除用户记录
     * 2. 物理删除，数据不可恢复
     * 3. 返回删除结果
     * 
     * 请求方式：DELETE
     * 请求路径：/user/delete/{id}
     * 
     * 请求示例：
     * DELETE /user/delete/5
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": "删除成功"}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "删除失败"}
     * 
     * 注意事项：
     * - 这是物理删除，数据会从数据库中永久删除
     * - 生产环境建议使用逻辑删除（添加deleted字段）
     * - 删除前应检查用户是否有关联数据
     * - 前端应有二次确认机制
     * 
     * @param id 要删除的用户ID，从URL路径中获取
     * @return CommonResult<String> 包含删除结果消息的统一响应
     */
    // @DeleteMapping：处理DELETE请求，RESTful规范中DELETE用于删除资源
    @DeleteMapping("/delete/{id}")
    public CommonResult<String> delete(@PathVariable Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的removeById方法，根据ID删除用户
        boolean result = userService.removeById(id);  // 执行：DELETE FROM user WHERE id = #{id}
        
        // 根据删除结果返回不同的响应
        if (result) {  // 删除成功（影响行数>0）
            // 返回成功结果和提示消息
            return CommonResult.success("删除成功");
        } else {  // 删除失败（可能ID不存在或数据库异常）
            // 返回错误结果和提示消息
            return CommonResult.error("删除失败");
        }
    }
}  // UserController类结束
