// 包声明：定义当前接口所属的包路径
package com.hwadee.mybatisplustest.mapper;

// 导入MyBatis-Plus的BaseMapper接口，提供通用CRUD方法
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
// 导入用户实体类
import com.hwadee.mybatisplustest.entity.User;
// 导入MyBatis的Mapper注解，标识该接口为Mapper层组件
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层(Mapper)接口
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的BaseMapper<User>接口，自动获得CRUD方法
 * 2. 与user表进行数据交互，执行增删改查SQL语句
 * 3. 无需编写XML映射文件，MyBatis-Plus自动生成SQL语句
 * 
 * 继承的BaseMapper提供的常用方法：
 * - insert(entity): 插入一条记录
 * - deleteById(id): 根据ID删除
 * - updateById(entity): 根据ID更新
 * - selectById(id): 根据ID查询
 * - selectOne(queryWrapper): 根据条件查询单个记录
 * - selectList(queryWrapper): 根据条件查询列表
 * - selectPage(page, queryWrapper): 分页查询
 * - selectCount(queryWrapper): 查询总记录数
 * 
 * 技术说明：
 * - @Mapper：MyBatis注解，标识该接口为Mapper层组件，Spring启动时自动扫描
 * - BaseMapper<User>：泛型指定User实体类，与user表映射
 * - 本接口无自定义方法，所有CRUD操作由BaseMapper提供
 * - 如需复杂SQL，可添加自定义方法并配合XML文件使用
 * 
 * 数据库表映射：
 * - 表名：user (通过User实体类的@TableName注解指定)
 * - 主键：id (Long类型，自增)
 * - 字段：username, password, role, age, email等
 * 
 * 智慧护理培训系统 - 用户数据访问层
 * @author young
 * @date 2025年10月21日 9:38
 */
// @Mapper：标识为MyBatis的Mapper接口，Spring启动时会自动扫描并生成代理实现类
@Mapper
// 接口定义：继承BaseMapper<User>，获得MyBatis-Plus提供的所有通用数据库操作方法
public interface UserMapper extends BaseMapper<User> {
    // 当前接口暂无自定义方法
    // 所有基础CRUD操作由BaseMapper接口提供
    // 如需添加复杂SQL查询，可在此声明自定义方法，例如：
    // @Select("SELECT * FROM user WHERE username = #{username} AND status = 1")
    // User selectActiveUserByUsername(@Param("username") String username);  // 查询激活用户
    // 
    // List<User> selectUsersByRole(@Param("role") String role);  // 根据角色查询用户列表
}  // UserMapper接口结束
