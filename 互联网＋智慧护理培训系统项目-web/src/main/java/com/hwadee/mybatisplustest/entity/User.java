// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.entity;

// 导入MyBatis-Plus的主键类型枚举
import com.baomidou.mybatisplus.annotation.IdType;
// 导入MyBatis-Plus的字段映射注解
import com.baomidou.mybatisplus.annotation.TableField;
// 导入MyBatis-Plus的主键注解
import com.baomidou.mybatisplus.annotation.TableId;
// 导入MyBatis-Plus的表名注解
import com.baomidou.mybatisplus.annotation.TableName;
// 导入Lombok的Data注解,自动生成getter/setter/toString等方法
import lombok.Data;

// 导入Java序列化接口
import java.io.Serializable;

/**
 * 用户实体类 - 对应数据库user表
 * 
 * 功能说明:
 * 1. 存储系统用户的完整信息(管理员、医生、护士、游客)
 * 2. 支持不同角色的权限区分
 * 3. 包含基本信息、联系方式、工作信息等字段
 * 
 * 技术说明:
 * - 使用Lombok简化代码,自动生成getter/setter
 * - 使用MyBatis-Plus简化数据库操作
 * - 实现Serializable接口支持序列化
 * 
 * @author young
 * @date 2025年10月21日 9:19
 * @version 1.6.0
 */
// @Data: Lombok注解,自动生成getter/setter/equals/hashCode/toString方法
@Data
// @TableName: 指定对应的数据库表名为"user"
@TableName("user")
// implements Serializable: 实现序列化接口,支持对象序列化和反序列化
public class User implements Serializable {

    // serialVersionUID: 序列化版本号,用于控制类的版本兼容性
    private static final long serialVersionUID = 1L;

    /**
     * 全参构造函数
     * 用于快速创建用户对象,包含基本必填字段
     * 
     * @param id 用户ID
     * @param username 用户名
     * @param age 年龄
     * @param email 邮箱
     * @param password 密码
     */
    public User(Long id, String username, Integer age, String email, String password) {
        this.id = id;               // 设置用户ID
        this.username = username;   // 设置用户名
        this.age = age;             // 设置年龄
        this.email = email;         // 设置邮箱
        this.password = password;   // 设置密码
    }

    /**
     * 无参构造函数
     * MyBatis-Plus要求实体类必须有无参构造函数
     * 用于反射创建对象实例
     */
    public User() {
        // 空构造函数,由框架调用
    }

    // ==================== 实体类字段定义 ====================
    
    /**
     * 用户ID - 主键
     * @TableId(type = IdType.AUTO): 指定主键生成策略为数据库自增
     * 类型: Long,范围更大,适合大数据量场景
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名 - 登录账号
     * @TableField("username"): 映射到数据库的username字段
     * 特点: 唯一,不能为空,用于登录验证
     */
    @TableField("username")
    private String username;

    /**
     * 年龄
     * @TableField("age"): 映射到数据库的age字段
     * 类型: Integer,可为null表示未填写
     */
    @TableField("age")
    private Integer age;

    /**
     * 邮箱地址
     * @TableField("email"): 映射到数据库的email字段
     * 用途: 联系方式、找回密码
     */
    @TableField("email")
    private String email;

    /**
     * 登录密码
     * @TableField("password"): 映射到数据库的password字段
     * 注意: 生产环境应使用加密存储(如BCrypt)
     */
    @TableField("password")
    private String password;

    /**
     * 用户角色
     * @TableField("role"): 映射到数据库的role字段
     * 可选值:
     * - "nurse": 护士角色,可管理病人、查看培训资料
     * - "doctor": 医生角色,可诊疗病人、查看培训资料
     * - "admin": 管理员角色,拥有所有权限
     * - "guest": 游客角色,仅可浏览部分内容
     */
    @TableField("role")
    private String role;  // nurse护士 或 doctor医生

    /**
     * 用户头像URL
     * @TableField("avatar"): 映射到数据库的avatar字段
     * 存储格式: 图片URL地址或base64编码
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 联系电话
     * @TableField("phone"): 映射到数据库的phone字段
     * 格式: 11位手机号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 家庭住址
     * @TableField("address"): 映射到数据库的address字段
     * 存储完整的地址信息
     */
    @TableField("address")
    private String address;

    /**
     * 所属部门
     * @TableField("department"): 映射到数据库的department字段
     * 例如: 内科、外科、急诊科、ICU等
     */
    @TableField("department")
    private String department;

    /**
     * 职位/职称
     * @TableField("position"): 映射到数据库的position字段
     * 例如: 主任医师、主治医师、护士长、护师等
     */
    @TableField("position")
    private String position;


    // ==================== Getter/Setter方法 ====================
    // 注意: 这些方法实际由@Data注解自动生成,此处手动编写是为了兼容性
    
    /**
     * 获取用户ID
     * @return 用户ID,Long类型
     */
    public Long getId() {
        return id;  // 返回用户ID字段的值
    }

    /**
     * 设置用户ID
     * @param id 要设置的用户ID
     */
    public void setId(Long id) {
        this.id = id;  // 将参数id赋值给实例变量id
    }

    /**
     * 获取用户名
     * @return 用户名字符串
     */
    public String getUsername() {
        return username;  // 返回用户名字段的值
    }

    /**
     * 设置用户名
     * @param username 要设置的用户名
     */
    public void setUsername(String username) {
        this.username = username;  // 将参数username赋值给实例变量username
    }

    /**
     * 获取年龄
     * @return 年龄,Integer类型,可能为null
     */
    public Integer getAge() {
        return age;  // 返回年龄字段的值
    }

    /**
     * 设置年龄
     * @param age 要设置的年龄
     */
    public void setAge(Integer age) {
        this.age = age;  // 将参数age赋值给实例变量age
    }

    /**
     * 获取邮箱地址
     * @return 邮箱地址字符串
     */
    public String getEmail() {
        return email;  // 返回邮箱字段的值
    }

    /**
     * 设置邮箱地址
     * @param email 要设置的邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;  // 将参数email赋值给实例变量email
    }

    /**
     * 获取密码
     * @return 密码字符串(注意:实际应用应加密存储)
     */
    public String getPassword() {
        return password;  // 返回密码字段的值
    }

    /**
     * 设置密码
     * @param password 要设置的密码
     */
    public void setPassword(String password) {
        this.password = password;  // 将参数password赋值给实例变量password
    }

    /**
     * 获取用户角色
     * @return 角色字符串(nurse/doctor/admin/guest)
     */
    public String getRole() {
        return role;  // 返回角色字段的值
    }

    /**
     * 设置用户角色
     * @param role 要设置的角色
     */
    public void setRole(String role) {
        this.role = role;  // 将参数role赋值给实例变量role
    }

    /**
     * 获取头像URL
     * @return 头像URL字符串
     */
    public String getAvatar() {
        return avatar;  // 返回头像字段的值
    }

    /**
     * 设置头像URL
     * @param avatar 要设置的头像URL
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;  // 将参数avatar赋值给实例变量avatar
    }

    /**
     * 获取联系电话
     * @return 电话号码字符串
     */
    public String getPhone() {
        return phone;  // 返回电话字段的值
    }

    /**
     * 设置联系电话
     * @param phone 要设置的电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone;  // 将参数phone赋值给实例变量phone
    }

    /**
     * 获取家庭住址
     * @return 地址字符串
     */
    public String getAddress() {
        return address;  // 返回地址字段的值
    }

    /**
     * 设置家庭住址
     * @param address 要设置的地址
     */
    public void setAddress(String address) {
        this.address = address;  // 将参数address赋值给实例变量address
    }

    /**
     * 获取所属部门
     * @return 部门名称字符串
     */
    public String getDepartment() {
        return department;  // 返回部门字段的值
    }

    /**
     * 设置所属部门
     * @param department 要设置的部门名称
     */
    public void setDepartment(String department) {
        this.department = department;  // 将参数department赋值给实例变量department
    }

    /**
     * 获取职位/职称
     * @return 职位字符串
     */
    public String getPosition() {
        return position;  // 返回职位字段的值
    }

    /**
     * 设置职位/职称
     * @param position 要设置的职位
     */
    public void setPosition(String position) {
        this.position = position;  // 将参数position赋值给实例变量position
    }

    /**
     * 重写toString方法
     * 用途: 将对象转换为可读的字符串格式,便于日志输出和调试
     * 
     * @return 包含所有字段信息的字符串
     */
    @Override
    public String toString() {
        return "User{" +                              // 返回格式化的字符串
                "id=" + id +                          // 拼接ID字段
                ", username='" + username + '\'' +    // 拼接用户名字段
                ", age=" + age +                      // 拼接年龄字段
                ", email='" + email + '\'' +          // 拼接邮箱字段
                ", password='" + password + '\'' +    // 拼接密码字段(注意:实际应用应隐藏密码)
                ", avatar='" + avatar + '\'' +        // 拼接头像字段
                ", phone='" + phone + '\'' +          // 拼接电话字段
                ", address='" + address + '\'' +      // 拼接地址字段
                ", department='" + department + '\'' + // 拼接部门字段
                ", position='" + position + '\'' +    // 拼接职位字段
                '}';                                   // 结束大括号
    }
}
