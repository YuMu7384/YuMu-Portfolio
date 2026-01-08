// 包声明：定义当前接口所属的包路径
package com.hwadee.mybatisplustest.service;

// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入MyBatis-Plus的IService接口，提供通用CRUD方法
import com.baomidou.mybatisplus.extension.service.IService;
// 桯入用户实体类
import com.hwadee.mybatisplustest.entity.User;

/**
 * 用户服务接口
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的IService<User>接口，自动获得CRUD方法
 * 2. 提供用户相关的业务逻辑接口定义
 * 3. 扩展了三个自定义查询方法
 * 
 * 继承的IService接口提供的常用方法：
 * - save(entity): 插入一条记录
 * - saveBatch(entityList): 批量插入
 * - removeById(id): 根据ID删除
 * - updateById(entity): 根据ID更新
 * - getById(id): 根据ID查询
 * - list(): 查询所有记录
 * - page(page): 分页查询
 * - count(): 查询总记录数
 * - lambdaQuery(): Lambda查询构造器
 * - lambdaUpdate(): Lambda更新构造器
 * 
 * 技术说明：
 * - IService是MyBatis-Plus提供的顶层服务接口
 * - 泛型<User>指定操作的实体类型
 * - 实现类需要继承ServiceImpl<UserMapper, User>
 * 
 * 智慧护理培训系统 - 用户服务层
 * @author young
 * @date 2025年10月21日 9:39
 */
// 接口定义：继承IService<User>，获得MyBatis-Plus提供的所有通用方法
public interface UserService extends IService<User>{
    /**
     * 根据ID获取用户信息
     * 
     * 功能说明：
     * 1. 通过用户ID查询用户完整信息
     * 2. 直接调用Mapper层进行数据库查询
     * 
     * 技术说明：
     * - 该方法与IService提供的getById(id)功能相同
     * - 可能是早期版本的自定义实现，实际可使用getById替代
     * 
     * @param id 用户ID，Long类型
     * @return User对象，如果ID不存在则返回null
     */
    User getUserById(Long id);  // 根据ID查询用户

    /**
     * 根据用户名获取用户信息
     * 
     * 功能说明：
     * 1. 通过用户名精确查询用户
     * 2. 使用QueryWrapper构造查询条件
     * 3. 常用于登录验证、用户名唯一性检查
     * 
     * 应用场景：
     * - 用户登录时根据用户名查找用户
     * - 注册时检查用户名是否已存在
     * - 忘记密码时验证用户名
     * 
     * @param username 用户名，String类型，不能为null
     * @return User对象，如果用户名不存在则返回null
     */
    User getUserByUsername(String username);  // 根据用户名查询用户

    /**
     * 分页查询用户列表
     * 
     * 功能说明：
     * 1. 根据页码和每页数量进行分页查询
     * 2. 返回包含总数和数据列表的Page对象
     * 3. 适用于后台管理系统的用户列表展示
     * 
     * 技术说明：
     * - 该方法与IService提供的page()功能相同
     * - 可能是早期版本的自定义实现
     * - 建议使用IService的page(new Page(pageNum, pageSize))方法替代
     * 
     * @param pageNum 页码，从1开始
     * @param pageSize 每页数量，建议10-50之间
     * @return Page<User> 分页对象，包含当前页数据和总记录数
     */
    Page<User> listByPage(Integer pageNum, Integer pageSize);  // 分页查询用户列表
}  // UserService接口结束
