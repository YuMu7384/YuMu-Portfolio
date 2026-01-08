// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器，用于类型安全的条件查询
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入病人实体类
import com.hwadee.mybatisplustest.entity.Patient;
// 导入病人服务接口
import com.hwadee.mybatisplustest.service.PatientService;
// 导入Jakarta EE的Resource注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入Spring Web的注解：RestController、RequestMapping、PathVariable、RequestBody等
import org.springframework.web.bind.annotation.*;

/**
 * 病人信息管理控制器
 * 
 * 功能说明：
 * 1. 提供病人信息的增删改查(CRUD)接口
 * 2. 支持关键词搜索(姓名、编号、电话)
 * 3. 支持按状态筛选(1=在院, 2=出院, 3=转院)
 * 4. 支持分页查询和列表查询
 * 
 * 使用场景：
 * - 医护人员管理病人基本信息
 * - 查看病人详细资料(病史、过敏史等)
 * - 更新护理等级和病人状态
 * 
 * 技术说明：
 * - @RestController：标识这是一个RESTful风格的控制器，自动将返回值转为JSON
 * - @RequestMapping：定义统一的请求路径前缀/patient和响应类型application/json
 * - @CrossOrigin：允许跨域请求，解决前后端分离的跨域问题
 * - Lambda查询：使用方法引用(Patient::getName)避免字符串字段名，类型安全
 * 
 * 智慧护理培训系统 - 病人管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解 = @Controller + @ResponseBody，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/patient，响应类型为application/json
@RequestMapping(value = "/patient", produces = "application/json")
// @CrossOrigin：允许所有来源的跨域请求，生产环境应限制具体域名
@CrossOrigin(origins = "*")
public class PatientController {

    /**
     * 病人服务层依赖注入
     * 
     * 技术说明：
     * - @Resource：JavaEE标准注解，按名称或类型自动注入PatientService实例
     * - 通过Spring容器自动装配PatientService，用于处理病人相关的业务逻辑
     */
    @Resource
    private PatientService patientService;  // 病人服务层接口，处理病人相关的业务逻辑

    /**
     * 获取病人列表 - 不分页
     * 
     * 功能说明：
     * 1. 支持关键词模糊搜索(姓名、病人编号、电话)
     * 2. 支持按状态精确筛选(1=在院, 2=出院, 3=转院)
     * 3. 按入院日期和ID降序排列(最新的排在前面)
     * 
     * 使用场景：
     * - 前端下拉框选择病人
     * - 导出所有病人数据
     * - 小量数据的列表展示
     * 
     * 请求方式：GET
     * 请求路径：/patient/list
     * 
     * 请求示例：
     * GET /patient/list?keyword=张三&status=1
     * 
     * 响应示例：
     * {"code": "200", "data": [{...}, {...}]}
     * 
     * @param keyword 搜索关键词(可选)，匹配姓名/编号/电话
     * @param status 病人状态(可选)，1=在院, 2=出院, 3=转院
     * @return CommonResult 包含病人列表数据
     */
    // @GetMapping：处理GET请求，映射到/patient/list路径
    @GetMapping("/list")
    public CommonResult<?> list(
            @RequestParam(value = "keyword", required = false) String keyword,  // @RequestParam：获取URL参数，required=false表示可选
            @RequestParam(value = "status", required = false) Integer status) {  // 状态参数，可选
        // 创建Lambda查询构造器，使用方法引用代替字符串字段名，类型安全
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();  // 实例化查询构造器
        
        // 判断关键词是否为空
        if (keyword != null && !keyword.isEmpty()) {  // 关键词不为空时执行搜索
            // 添加模糊搜索条件：姓名 LIKE '%keyword%' OR 编号 LIKE '%keyword%' OR 电话 LIKE '%keyword%'
            wrapper.like(Patient::getName, keyword)          // 姓名模糊匹配
                   .or().like(Patient::getPatientNo, keyword)  // OR 病人编号模糊匹配
                   .or().like(Patient::getPhone, keyword);      // OR 电话号码模糊匹配
        }
        // 判断状态是否为空
        if (status != null) {  // 状态不为空时添加筛选条件
            // 添加等值匹配条件：AND status = #{status}
            wrapper.eq(Patient::getStatus, status);  // 精确匹配状态
        }
        // 添加排序条件：先按入院日期降序，再按ID降序
        wrapper.orderByDesc(Patient::getAdmissionDate)  // ORDER BY admission_date DESC
               .orderByDesc(Patient::getId);              // ORDER BY id DESC
        
        // 调用Service层的list方法，执行查询并返回结果列表
        return CommonResult.success(patientService.list(wrapper));  // 返回成功响应和病人列表
    }

    /**
     * 分页查询病人列表
     * 
     * 功能说明：
     * 1. 支持分页显示，默认10条/页
     * 2. 支持关键词搜索和状态筛选
     * 3. 返回分页对象，包含总数、当前页等信息
     * 
     * 使用场景：
     * - 后台管理系统的病人列表页面
     * - 大量数据的分页展示
     * - 需要显示总数和页码的场景
     * 
     * 请求方式：GET
     * 请求路径：/patient/page
     * 
     * 请求示例：
     * GET /patient/page?pageNo=1&pageSize=10&keyword=张三&status=1
     * 
     * 响应示例：
     * {
     *   "code": "200",
     *   "data": {
     *     "records": [{...}, {...}],  // 当前页数据
     *     "total": 50,                 // 总记录数
     *     "current": 1,                // 当前页码
     *     "size": 10                   // 每页数量
     *   }
     * }
     * 
     * @param pageNo 页码，从1开始，默认1
     * @param pageSize 每页数量，默认10
     * @param keyword 搜索关键词，匹配姓名/编号/电话
     * @param status 病人状态，1=在院, 2=出院, 3=转院
     * @return CommonResult<Page<Patient>> 分页结果
     */
    // @GetMapping：处理GET请求，映射到/patient/page路径
    @GetMapping("/page")
    public CommonResult<Page<Patient>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码，默认1
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量，默认10
            @RequestParam(value = "keyword", required = false) String keyword,    // 关键词，可选
            @RequestParam(value = "status", required = false) Integer status) {    // 状态，可选
        // 创建分页对象，指定当前页码和每页数量
        Page<Patient> page = new Page<>(pageNo, pageSize);  // MyBatis-Plus的Page对象
        
        // 创建Lambda查询构造器，构造查询条件
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();  // 实例化查询构造器
        
        // 判断关键词是否为空
        if (keyword != null && !keyword.isEmpty()) {  // 关键词不为空时添加搜索条件
            // 添加模糊搜索：姓名 OR 编号 OR 电话
            wrapper.like(Patient::getName, keyword)          // 姓名 LIKE '%keyword%'
                   .or().like(Patient::getPatientNo, keyword)  // OR patient_no LIKE '%keyword%'
                   .or().like(Patient::getPhone, keyword);      // OR phone LIKE '%keyword%'
        }
        // 判断状态是否为空
        if (status != null) {  // 状态不为空时添加筛选条件
            // 添加精确匹配条件
            wrapper.eq(Patient::getStatus, status);  // AND status = #{status}
        }
        // 添加排序条件：先按入院日期降序，再按ID降序
        wrapper.orderByDesc(Patient::getAdmissionDate)  // ORDER BY admission_date DESC
               .orderByDesc(Patient::getId);              // , id DESC
        
        // 调用Service层的page方法，执行分页查询
        patientService.page(page, wrapper);  // 自动填充page对象的records和total属性
        
        // 返回分页结果
        return CommonResult.success(page);  // 返回成功响应和分页数据
    }

    /**
     * 根据ID获取病人详情
     * 
     * 功能说明：
     * 1. 查询单个病人的完整信息
     * 2. 用于查看详情页面
     * 3. 验证病人是否存在
     * 
     * 使用场景：
     * - 点击列表查看病人详情
     * - 编辑病人信息前获取数据
     * - 查看病人病史、过敏史等详细信息
     * 
     * 请求方式：GET
     * 请求路径：/patient/get/{id}
     * 
     * 请求示例：
     * GET /patient/get/1
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": {"id": 1, "name": "张三", ...}}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "未找到病人信息"}
     * 
     * @param id 病人ID，从URL路径中获取
     * @return CommonResult<Patient> 病人详细信息或错误信息
     */
    // @GetMapping：处理GET请求，{id}为路径变量占位符
    @GetMapping("/get/{id}")
    public CommonResult<Patient> get(@PathVariable("id") Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的getById方法，根据ID查询病人
        Patient patient = patientService.getById(id);  // 执行：SELECT * FROM patient WHERE id = #{id}
        
        // 使用三元运算符判断病人是否存在，返回相应结果
        return patient != null   // 如果病人对象不为null
                ? CommonResult.success(patient)        // 返回成功响应和病人数据
                : CommonResult.error("未找到病人信息");  // 返回错误响应
    }

    /**
     * 创建新病人信息
     * 
     * 功能说明：
     * 1. 添加新病人到数据库
     * 2. 自动生成ID，无需传入
     * 3. 返回保存后的病人对象(包含生成的ID)
     * 
     * 使用场景：
     * - 医护人员添加新入院病人
     * - 批量导入病人数据
     * 
     * 请求方式：POST
     * 请求路径：/patient/create
     * 请求体格式：application/json
     * 
     * 请求示例：
     * POST /patient/create
     * Body: {
     *   "patientNo": "P20250001",
     *   "name": "张三",
     *   "gender": 1,
     *   "age": 65,
     *   "phone": "13800138000",
     *   "diagnosis": "高血压",
     *   "careLevel": "一级护理",
     *   "status": 1,
     *   "admissionDate": "2025-01-01"
     * }
     * 
     * 响应示例：
     * {"code": "200", "data": {"id": 10, "patientNo": "P20250001", ...}}
     * 
     * 注意事项：
     * - 即使前端传入id，也会被设置为null，确保是创建操作
     * - 病人编号patientNo应该唯一，建议前端校验
     * 
     * @param patient 病人对象，由@RequestBody将JSON转换为Patient实体
     * @return CommonResult<Patient> 保存后的病人信息(包含自动生成的ID)
     */
    // @PostMapping：处理POST请求，用于创建新资源
    @PostMapping("/create")
    public CommonResult<Patient> create(@RequestBody Patient patient) {  // @RequestBody：将JSON请求体转换为Patient对象
        // 将ID设置为null，确保是创建操作而不是更新操作
        patient.setId(null);  // 即使前端传入id，也强制设置为null
        
        // 调用Service层的save方法，将病人信息保存到数据库
        patientService.save(patient);  // 执行：INSERT INTO patient (...) VALUES (...)
        
        // 返回成功响应，包含保存后的病人对象（此时patient.id已被赋值）
        return CommonResult.success(patient);  // MyBatis-Plus会自动回填id值
    }

    /**
     * 更新病人信息
     * 
     * 功能说明：
     * 1. 根据ID更新病人信息
     * 2. 支持部分字段更新（只更新非空字段）
     * 3. 用于修改护理等级、状态等字段
     * 
     * 使用场景：
     * - 修改病人基本信息
     * - 更新护理等级
     * - 更改病人状态(在院→出院)
     * - 更新诊断信息
     * 
     * 请求方式：POST
     * 请求路径：/patient/update
     * 请求体格式：application/json
     * 
     * 请求示例：
     * POST /patient/update
     * Body: {
     *   "id": 1,
     *   "careLevel": "特级护理",
     *   "status": 1
     * }
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": true}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "更新失败"}
     * 
     * 注意事项：
     * - 必须包含id字段，否则无法定位要更新的记录
     * - 只更新传入的非空字段
     * 
     * @param patient 病人对象，必须包含id字段
     * @return CommonResult<Boolean> 更新是否成功
     */
    // @PostMapping：处理POST请求，用于更新操作
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody Patient patient) {  // @RequestBody：将JSON请求体转换为Patient对象
        // 调用Service层的updateById方法，根据ID更新病人信息
        boolean ok = patientService.updateById(patient);  // 执行：UPDATE patient SET ... WHERE id = #{id}
        
        // 根据更新结果返回不同的响应
        return ok   // 如果更新成功(ok=true)
                ? CommonResult.success(true)    // 返回成功响应
                : CommonResult.error("更新失败");  // 返回失败响应
    }

    /**
     * 删除病人信息
     * 
     * 功能说明：
     * 1. 根据ID物理删除病人记录
     * 2. 删除后数据不可恢复
     * 3. 返回删除结果
     * 
     * 使用场景：
     * - 删除错误录入的病人信息
     * - 清理历史数据
     * 
     * 请求方式：DELETE
     * 请求路径：/patient/delete/{id}
     * 
     * 请求示例：
     * DELETE /patient/delete/5
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": true}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "删除失败"}
     * 
     * 注意事项：
     * - 这是物理删除，数据会从数据库中永久删除
     * - 前端应该有二次确认机制
     * - 生产环境建议使用逻辑删除而非物理删除
     * - 删除前应检查是否有关联数据(如护理记录等)
     * 
     * @param id 病人ID，从URL路径中获取
     * @return CommonResult<Boolean> 删除是否成功
     */
    // @DeleteMapping：处理DELETE请求，RESTful规范中DELETE用于删除资源
    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的removeById方法，根据ID删除病人
        boolean ok = patientService.removeById(id);  // 执行：DELETE FROM patient WHERE id = #{id}
        
        // 根据删除结果返回不同的响应
        return ok   // 如果删除成功(ok=true)
                ? CommonResult.success(true)    // 返回成功响应
                : CommonResult.error("删除失败");  // 返回失败响应
    }
}  // PatientController类结束


