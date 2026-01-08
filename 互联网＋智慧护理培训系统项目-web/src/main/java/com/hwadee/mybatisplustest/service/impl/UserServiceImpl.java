// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.service.impl;

// 导入MyBatis-Plus的QueryWrapper类，用于构造查询条件
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入MyBatis-Plus的ServiceImpl基类，提供通用CRUD实现
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// 导入用户实体类
import com.hwadee.mybatisplustest.entity.User;
// 导入用户Mapper接口
import com.hwadee.mybatisplustest.mapper.UserMapper;
// 导入用户服务接口
import com.hwadee.mybatisplustest.service.UserService;
// 导入Jakarta EE的Resource注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入Spring的Service注解，标识为服务层组件
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 
 * 功能说明：
 * 1. 继承ServiceImpl<UserMapper, User>，自动获得MyBatis-Plus提供的CRUD方法
 * 2. 实现UserService接口，提供用户相关的业务逻辑实现
 * 3. 实现了三个自定义查询方法：按ID查询、按用户名查询、分页查询
 * 
 * 类层次结构：
 * - extends ServiceImpl<UserMapper, User>: 继承MyBatis-Plus基础服务实现
 *   - 第一个泛型参数UserMapper: 指定Mapper接口
 *   - 第二个泛型参数User: 指定实体类
 * - implements UserService: 实现用户服务接口
 * 
 * 技术说明：
 * - @Service：标识该类为Spring的服务层组件，交由Spring容器管理
 * - ServiceImpl提供了baseMapper属性，可直接调用Mapper方法
 * - 注入的userMapper主要用于自定义方法，通用方法使用baseMapper
 * 
 * 智慧护理培训系统 - 用户服务层实现
 * @author young
 * @date 2025年10月21日 9:39
 */
// @Service：标识为Spring服务层组件，启动时自动扫描并注册到Spring容器
@Service
// 类定义：继承ServiceImpl获取通用CRUD方法，实现UserService接口
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    /**
     * 用户Mapper层依赖注入
     * 
     * 技术说明：
     * - @Resource：JavaEE标准注解，按名称或类型自动注入UserMapper实例
     * - 虽然ServiceImpl已提供baseMapper属性，但显式注入更清晰
     * - 用于直接调用Mapper层的数据库操作方法
     */
    @Resource
    private UserMapper userMapper;  // 用户数据访问层接口

    /**
     * 根据ID获取用户信息
     * 
     * 功能说明：
     * 1. 通过用户ID查询用户完整信息
     * 2. 直接调用Mapper层的selectById方法
     * 3. 执行SQL：SELECT * FROM user WHERE id = #{id}
     * 
     * 技术说明：
     * - @Override：重写UserService接口中的抽象方法
     * - userMapper.selectById()：MyBatis-Plus提供的根据主键查询方法
     * - 该方法功能与IService的getById(id)相同，实际可直接使用getById
     * 
     * @param id 用户ID，Long类型，不能为null
     * @return User对象，包含用户完整信息；如果ID不存在则返回null
     */
    @Override  // 重写接口方法
    public User getUserById(Long id) {  // 方法签名必须与接口中的定义一致
        // 调用Mapper层的selectById方法，根据主键ID查询用户
        return userMapper.selectById(id);  // 返回查询结果（User对象或null）
    }

    /**
     * 根据用户名获取用户信息
     * 
     * 功能说明：
     * 1. 通过用户名精确查询用户
     * 2. 使用QueryWrapper构造查询条件
     * 3. 执行SQL：SELECT * FROM user WHERE username = #{username}
     * 
     * 应用场景：
     * - 用户登录时根据用户名查找用户
     * - 注册时检查用户名是否已存在
     * - 忘记密码时验证用户名
     * 
     * 技术说明：
     * - QueryWrapper：MyBatis-Plus的条件构造器，用于构建 SQL WHERE 条件
     * - eq()方法：添加等值匹配条件 (equals)
     * - selectOne()：查询单个记录，如果结果多于1条会抛异常
     * 
     * @param username 用户名，String类型，不能为null
     * @return User对象，包含用户完整信息；如果用户名不存在则返回null
     */
    @Override  // 重写接口方法
    public User getUserByUsername(String username) {  // 参数：用户名
        // 创建查询条件构造器，泛型指定为User实体类
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  // 实例化QueryWrapper对象
        // 添加查询条件：username = 参数username
        queryWrapper.eq("username", username);  // 生成SQL：WHERE username = 'xxx'
        // 执行查询，返回单个结果（如果结果备0条返回null，备1条返回该记录，>1条抛异常）
        return userMapper.selectOne(queryWrapper);  // 返回查询结果
    }

    /**
     * 分页查询用户列表
     * 
     * 功能说明：
     * 1. 根据页码和每页数量进行分页查询
     * 2. 查询所有用户，不加任何过滤条件
     * 3. 返回包含总数和数据列表的Page对象
     * 
     * 分页逻辑：
     * - 第1页，每靓10条：查询第1-10条记录 (LIMIT 10 OFFSET 0)
     * - 第2页，每靓10条：查询第11-20条记录 (LIMIT 10 OFFSET 10)
     * - OFFSET = (pageNum - 1) * pageSize
     * 
     * 技术说明：
     * - Page<User>：MyBatis-Plus的分页对象，包含records、total、current等属性
     * - selectPage()：执行分页查询，第一个参数为分页对象，第二个参数为查询条件
     * - null作为条件表示不加任何WHERE条件
     * - 该方法功能与IService的page(new Page(pageNum, pageSize))相同
     * 
     * @param pageNum 页码，从1开始，必须>0
     * @param pageSize 每页数量，建议10-50之间，必须>0
     * @return Page<User> 分页对象，包含：
     *         - records：当前页的用户列表 (List<User>)
     *         - total：总记录数 (Long)
     *         - current：当前页码 (Long)
     *         - size：每页数量 (Long)
     *         - pages：总页数 (Long)
     */
    @Override  // 重写接口方法
    public Page<User> listByPage(Integer pageNum, Integer pageSize) {  // 参数：页码和每页数量
        // 创建分页对象，指定当前页码和每页数量
        Page<User> page = new Page<>(pageNum, pageSize);  // 实例化Page对象
        // 执行分页查询，第二个参数null表示不加WHERE条件
        userMapper.selectPage(page, null);  // SQL：SELECT * FROM user LIMIT pageSize OFFSET (pageNum-1)*pageSize
        // 返回填充好数据的Page对象（selectPage会自动填充page对象的records和total属性）
        return page;  // 返回分页结果
    }
}  // UserServiceImpl类结束
