// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.common;

/**
 * 通用响应结果封装类
 * 
 * 功能说明：
 * 1. 封装所有API接口的统一响应格式
 * 2. 包含状态码、提示信息、业务数据三部分
 * 3. 使用泛型<T>支持任意类型的数据返回
 * 4. 提供静态工厂方法快速创建成功/失败响应
 * 
 * 响应结构示例：
 * {
 *   "code": "200",         // 状态码：200-成功，500-失败
 *   "message": "操作成功",  // 提示信息
 *   "data": {...}          // 业务数据，可为对象、数组、基本类型等
 * }
 * 
 * 使用示例：
 * // 成功响应
 * return CommonResult.success(user);  // 返回用户对象
 * return CommonResult.success(userList);  // 返回用户列表
 * 
 * // 失败响应
 * return CommonResult.error("用户不存在");  // 返回错误信息
 * return CommonResult.error("参数校验失败");
 * 
 * 技术说明：
 * - 泛型<T>：使用Java泛型支持多种数据类型
 * - 静态方法：success()和error()方法可直接通过类名调用
 * - 链式调用：可以new CommonResult(...).setCode(...).setMessage(...)
 * 
 * 智慧护理培训系统 - 通用响应类
 * @author young
 * @date 2025年10月27日 11:09
 * @param <T> 业务数据类型，可为任意类型（User、List<User>、String等）
 */
public class CommonResult<T> {  // 泛型类定义，<T>表示数据类型可变
    /**
     * 状态码
     * 约定：
     * - "200"：请求成功
     * - "500"：服务器错误或业务异常
     * - 可扩展其他状态码："401"-未授权, "403"-禁止访问, "404"-资源不存在等
     */
    private String code;  // 响应状态码
    
    /**
     * 提示信息
     * 用途：
     * - 成功时显示操作成功信息，如"操作成功"、"保存成功"
     * - 失败时显示错误原因，如"用户不存在"、"参数校验失败"
     * - 前端通常会将此信息展示给用户
     */
    private String message;  // 响应提示消息
    
    /**
     * 业务数据
     * 类型：
     * - 单个对象：User, Patient, Article等
     * - 集合对象：List<User>, Page<Patient>等
     * - 基本类型：String, Integer, Boolean等
     * - null：失败响应或无需返回数据时为null
     */
    private T data;  // 业务数据，泛型<T>可为任意类型

    /**
     * 全参数构造函数
     * 
     * 功能说明：
     * 1. 初始化CommonResult对象的所有属性
     * 2. 通过构造函数一次性设置状态码、信息、数据
     * 3. 通常不直接调用，建议使用success()/error()静态方法
     * 
     * 使用示例：
     * CommonResult<User> result = new CommonResult<>("200", "查询成功", user);
     * 
     * @param code 状态码，如"200"(成功)、"500"(失败)
     * @param message 提示信息，如"操作成功"、"用户不存在"
     * @param data 业务数据，可为任意类型或null
     */
    public CommonResult(String code, String message, T data) {  // 构造方法，接收三个参数
        this.code = code;        // 设置状态码
        this.message = message;  // 设置提示信息
        this.data = data;        // 设置业务数据
    }

    /**
     * 成功响应工厂方法
     * 
     * 功能说明：
     * 1. 快速创建一个表示成功的响应对象
     * 2. 自动设置状态码为"200"，信息为"操作成功"
     * 3. 将传入的数据封装到data字段
     * 
     * 使用场景：
     * - 查询成功：return CommonResult.success(user);
     * - 创建成功：return CommonResult.success(newUser);
     * - 更新成功：return CommonResult.success(updatedUser);
     * - 删除成功：return CommonResult.success("删除成功");
     * 
     * 技术说明：
     * - static：静态方法，可直接通过类名调用
     * - <T>：方法泛型声明，使该方法支持任意类型
     * - new CommonResult<>()：调用构造函数创建实例
     * 
     * @param <T> 数据类型，自动推断
     * @param data 业务数据，可为任意类型
     * @return CommonResult<T> 成功响应对象，code="200", message="操作成功"
     */
    public static <T> CommonResult<T> success(T data) {  // 静态泛型方法，返回成功响应
        // 创建并返回一个新的CommonResult对象，状态码200，固定消息"操作成功"，数据为传入的data
        return new CommonResult<>("200", "操作成功", data);  // 调用构造函数
    }

    /**
     * 失败响应工厂方法
     * 
     * 功能说明：
     * 1. 快速创建一个表示失败的响应对象
     * 2. 自动设置状态码为"500"，data为null
     * 3. 自定义错误信息，告诉前端具体错误原因
     * 
     * 使用场景：
     * - 数据不存在：return CommonResult.error("用户不存在");
     * - 参数错误：return CommonResult.error("用户名不能为空");
     * - 权限不足：return CommonResult.error("无权访问");
     * - 业务异常：return CommonResult.error("密码错误");
     * 
     * 技术说明：
     * - static：静态方法，可直接通过类名调用
     * - <T>：方法泛型声明，即使失败也需要保持类型一致
     * - data设为null：失败响应通常不返回业务数据
     * 
     * @param <T> 数据类型，保持与调用方类型一致
     * @param message 错误信息，描述具体错误原因
     * @return CommonResult<T> 失败响应对象，code="500", data=null
     */
    public static <T> CommonResult<T> error(String message) {  // 静态泛型方法，返回失败响应
        // 创建并返回一个新的CommonResult对象，状态码500，自定义错误消息，数据为null
        return new CommonResult<>("500", message, null);  // 调用构造函数，数据为null
    }

    // ==================== Getter和Setter方法 ====================
    // 用于获取和设置对象的属性值
    // JSON序列化时会自动调用这些getter方法
    
    /**
     * 获取状态码
     * @return 状态码字符串，如"200"、"500"
     */
    public String getCode() {  // 获取code属性
        return code;  // 返回状态码
    }

    /**
     * 设置状态码
     * @param code 要设置的状态码，如"200"、"500"、"401"等
     */
    public void setCode(String code) {  // 设置code属性
        this.code = code;  // 将参数值赋给实例变量
    }

    /**
     * 获取提示信息
     * @return 提示信息字符串，如"操作成功"、"用户不存在"
     */
    public String getMessage() {  // 获取message属性
        return message;  // 返回提示信息
    }

    /**
     * 设置提示信息
     * @param message 要设置的提示信息
     */
    public void setMessage(String message) {  // 设置message属性
        this.message = message;  // 将参数值赋给实例变量
    }

    /**
     * 获取业务数据
     * @return 业务数据对象，类型为泛型<T>，可为任意类型或null
     */
    public T getData() {  // 获取data属性
        return data;  // 返回业务数据
    }

    /**
     * 设置业务数据
     * @param data 要设置的业务数据，类型为泛型<T>
     */
    public void setData(T data) {  // 设置data属性
        this.data = data;  // 将参数值赋给实例变量
    }

    /**
     * 对象字符串表示
     * 
     * 功能说明：
     * 1. 将CommonResult对象转换为可读的字符串格式
     * 2. 便于日志输出和调试信息查看
     * 3. 重写Object类的toString()方法
     * 
     * 输出示例：
     * CommonResult{code='200', message='操作成功', data=User{id=1, username='admin'}}
     * 
     * 使用场景：
     * - 日志记录：log.info("Response: " + result.toString());
     * - 控制台输出：System.out.println(result);
     * - 调试信息：直接打印对象即可查看内容
     * 
     * @return 对象的字符串表示，包含所有属性值
     */
    @Override  // 重写Object类的toString()方法
    public String toString() {  // toString方法，返回String类型
        // 使用字符串拼接，构建格式化输出
        return "CommonResult{" +  // 类名开始
                "code='" + code + '\'' +  // 拼接code字段
                ", message='" + message + '\'' +  // 拼接message字段
                ", data=" + data +  // 拼接data字段（自动调用data对象的toString()）
                '}';  // 结束大括号
    }
}  // CommonResult类结束
