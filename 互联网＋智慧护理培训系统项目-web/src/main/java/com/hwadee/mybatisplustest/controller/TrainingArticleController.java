// 包声明：定义当前类所属的包路径
package com.hwadee.mybatisplustest.controller;

// 导入MyBatis-Plus的Lambda查询构造器，用于类型安全的条件查询
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus的分页插件Page类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// 导入统一响应结果封装类
import com.hwadee.mybatisplustest.common.CommonResult;
// 导入培训文章实体类
import com.hwadee.mybatisplustest.entity.TrainingArticle;
// 导入培训文章服务接口
import com.hwadee.mybatisplustest.service.TrainingArticleService;
// 导入Jakarta EE的Resource注解，用于依赖注入
import jakarta.annotation.Resource;
// 导入Spring Web的注解：RestController、RequestMapping、PathVariable、RequestBody等
import org.springframework.web.bind.annotation.*;

/**
 * 培训文章管理控制器
 * 
 * 功能说明：
 * 1. 提供培训文章的增删改查(CRUD)接口
 * 2. 支持按标题关键词搜索文章
 * 3. 支持按分类ID筛选文章
 * 4. 支持分页查询和列表查询
 * 5. 只展示已发布的文章（发布状态=1）
 * 
 * 使用场景：
 * - 医护人员浏览培训文章
 * - 按分类查看护理知识文章
 * - 搜索特定主题的培训内容
 * - 管理员管理培训文章
 * 
 * 技术说明：
 * - @RestController：标识这是一个RESTful风格的控制器，自动将返回值转为JSON
 * - @RequestMapping：定义统一的请求路径前缀/training/article
 * - @CrossOrigin：允许跨域请求，解决前后端分离的跨域问题
 * - Lambda查询：使用方法引用保证类型安全
 * 
 * 智慧护理培训系统 - 培训文章管理模块
 * @author AI Assistant
 * @version 1.6.0
 */
// @RestController：组合注解 = @Controller + @ResponseBody，自动将方法返回值序列化为JSON
@RestController
// @RequestMapping：类级别的请求映射，所有接口URL前缀为/training/article
@RequestMapping(value = "/training/article", produces = "application/json")
// @CrossOrigin：允许所有来源的跨域请求，生产环境应限制具体域名
@CrossOrigin(origins = "*")
public class TrainingArticleController {

    /**
     * 培训文章服务层依赖注入
     * 
     * 技术说明：
     * - @Resource：JavaEE标准注解，按名称或类型自动注入TrainingArticleService实例
     * - 通过Spring容器自动装配TrainingArticleService，用于处理培训文章相关的业务逻辑
     */
    @Resource
    private TrainingArticleService trainingArticleService;  // 培训文章服务层接口

    /**
     * 创建新培训文章
     * 
     * 功能说明：
     * 1. 添加新培训文章到数据库
     * 2. 自动生成ID，无需传入
     * 3. 返回保存后的文章对象（包含生成的ID）
     * 
     * 使用场景：
     * - 管理员发布新的培训文章
     * - 批量导入培训资料
     * 
     * 请求方式：POST
     * 请求路径：/training/article/create
     * 请求体格式：application/json
     * 
     * 请求示例：
     * POST /training/article/create
     * Body: {
     *   "title": "糖尿病护理指南",
     *   "content": "文章内容...",
     *   "categoryId": 1,
     *   "publishStatus": 1,
     *   "publishAt": "2025-01-01 10:00:00"
     * }
     * 
     * 响应示例：
     * {"code": "200", "data": {"id": 10, "title": "糖尿病护理指南", ...}}
     * 
     * @param article 培训文章对象，由@RequestBody将JSON转换为TrainingArticle实体
     * @return CommonResult<TrainingArticle> 保存后的文章信息（包含自动生成的ID）
     */
    // @PostMapping：处理POST请求，用于创建新资源
    @PostMapping("/create")
    public CommonResult<TrainingArticle> create(@RequestBody TrainingArticle article) {  // @RequestBody：将JSON请求体转换为TrainingArticle对象
        // 将ID设置为null，确保是创建操作而不是更新操作
        article.setId(null);  // 即使前端传入id，也强制设置为null
        // 调用Service层的save方法，将文章信息保存到数据库
        trainingArticleService.save(article);  // 执行：INSERT INTO training_article (...) VALUES (...)
        // 返回成功响应，包含保存后的文章对象（此时article.id已被赋值）
        return CommonResult.success(article);  // MyBatis-Plus会自动回填id值
    }

    /**
     * 更新培训文章信息
     * 
     * 功能说明：
     * 1. 根据ID更新培训文章信息
     * 2. 支持部分字段更新（只更新传入的非空字段）
     * 3. 用于修改文章标题、内容、分类、发布状态等
     * 
     * 使用场景：
     * - 管理员修改文章内容
     * - 更新文章分类
     * - 修改发布状态（发布/撤回）
     * 
     * 请求方式：POST
     * 请求路径：/training/article/update
     * 请求体格式：application/json
     * 
     * 请求示例：
     * POST /training/article/update
     * Body: {
     *   "id": 1,
     *   "title": "糖尿病护理指南（修订版）",
     *   "publishStatus": 1
     * }
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": true}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "更新失败"}
     * 
     * @param article 培训文章对象，必须包含id字段
     * @return CommonResult<Boolean> 更新是否成功
     */
    // @PostMapping：处理POST请求，用于更新操作
    @PostMapping("/update")
    public CommonResult<Boolean> update(@RequestBody TrainingArticle article) {  // @RequestBody：将JSON请求体转换为TrainingArticle对象
        // 调用Service层的updateById方法，根据ID更新文章信息
        boolean ok = trainingArticleService.updateById(article);  // 执行：UPDATE training_article SET ... WHERE id = #{id}
        // 根据更新结果返回不同的响应
        return ok   // 如果更新成功(ok=true)
                ? CommonResult.success(true)    // 返回成功响应
                : CommonResult.error("更新失败");  // 返回失败响应
    }

    /**
     * 删除培训文章
     * 
     * 功能说明：
     * 1. 根据ID物理删除培训文章记录
     * 2. 删除后数据不可恢复
     * 
     * 使用场景：
     * - 删除过时的培训文章
     * - 删除错误录入的文章
     * 
     * 请求方式：GET（注意：应使用DELETE方法，这里可能是历史遗留）
     * 请求路径：/training/article/delete/{id}
     * 
     * 请求示例：
     * GET /training/article/delete/5
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
     * - 建议改用@DeleteMapping注解更符合RESTful规范
     * 
     * @param id 文章ID，从URL路径中获取
     * @return CommonResult<Boolean> 删除是否成功
     */
    // @GetMapping：处理GET请求（注意：删除操作应使用@DeleteMapping更规范）
    @GetMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的removeById方法，根据ID删除文章
        boolean ok = trainingArticleService.removeById(id);  // 执行：DELETE FROM training_article WHERE id = #{id}
        // 根据删除结果返回不同的响应
        return ok   // 如果删除成功(ok=true)
                ? CommonResult.success(true)    // 返回成功响应
                : CommonResult.error("删除失败");  // 返回失败响应
    }

    /**
     * 根据ID获取培训文章详情
     * 
     * 功能说明：
     * 1. 查询单个培训文章的完整信息
     * 2. 用于查看文章详情页面
     * 3. 验证文章是否存在
     * 
     * 使用场景：
     * - 用户点击文章标题查看详情
     * - 编辑文章前获取原始数据
     * - 阅读培训文章内容
     * 
     * 请求方式：GET
     * 请求路径：/training/article/get/{id}
     * 
     * 请求示例：
     * GET /training/article/get/1
     * 
     * 响应示例（成功）：
     * {"code": "200", "data": {"id": 1, "title": "糖尿病护理指南", ...}}
     * 
     * 响应示例（失败）：
     * {"code": "500", "message": "未找到"}
     * 
     * @param id 文章ID，从URL路径中获取
     * @return CommonResult<TrainingArticle> 文章详细信息或错误信息
     */
    // @GetMapping：处理GET请求，{id}为路径变量占位符
    @GetMapping("/get/{id}")
    public CommonResult<TrainingArticle> get(@PathVariable("id") Long id) {  // @PathVariable：从URL路径提取id参数
        // 调用Service层的getById方法，根据ID查询文章
        TrainingArticle one = trainingArticleService.getById(id);  // 执行：SELECT * FROM training_article WHERE id = #{id}
        // 使用三元运算符判断文章是否存在，返回相应结果
        return one != null   // 如果文章对象不为null
                ? CommonResult.success(one)        // 返回成功响应和文章数据
                : CommonResult.error("未找到");  // 返回错误响应
    }

    /**
     * 获取培训文章列表 - 不分页
     * 
     * 功能说明：
     * 1. 支持按标题关键词模糊搜索
     * 2. 支持按分类ID精确筛选
     * 3. 只返回已发布的文章（publishStatus=1）
     * 4. 按发布时间和ID降序排列（最新的排在前面）
     * 
     * 使用场景：
     * - 首页展示最新培训文章
     * - 按分类浏览文章列表
     * - 搜索特定主题的文章
     * 
     * 请求方式：GET
     * 请求路径：/training/article/list
     * 
     * 请求示例：
     * GET /training/article/list?keyword=糖尿病&categoryId=1
     * 
     * 响应示例：
     * {"code": "200", "data": [{...}, {...}]}
     * 
     * @param keyword 搜索关键词（可选），匹配文章标题
     * @param categoryId 分类ID（可选），精确匹配分类
     * @return CommonResult 包含文章列表数据
     */
    // @GetMapping：处理GET请求，映射到/training/article/list路径
    @GetMapping("/list")
    public CommonResult<?> list(
            @RequestParam(value = "keyword", required = false) String keyword,      // @RequestParam：获取URL参数，required=false表示可选
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID参数，可选
        // 创建Lambda查询构造器，使用方法引用代替字符串字段名，类型安全
        LambdaQueryWrapper<TrainingArticle> wrapper = new LambdaQueryWrapper<>();  // 实例化查询构造器
        
        // 判断关键词是否为空
        if (keyword != null && !keyword.isEmpty()) {  // 关键词不为空时执行搜索
            // 添加模糊搜索条件：标题 LIKE '%keyword%'
            wrapper.like(TrainingArticle::getTitle, keyword);  // 标题模糊匹配
        }
        // 判断分类ID是否为空
        if (categoryId != null) {  // 分类ID不为空时添加筛选条件
            // 添加等值匹配条件：AND category_id = #{categoryId}
            wrapper.eq(TrainingArticle::getCategoryId, categoryId);  // 精确匹配分类
        }
        // 添加发布状态过滤条件：只返回已发布的文章
        wrapper.eq(TrainingArticle::getPublishStatus, 1);  // AND publish_status = 1（1=已发布）
        
        // 添加排序条件：先按发布时间降序，再按ID降序
        wrapper.orderByDesc(TrainingArticle::getPublishAt)  // ORDER BY publish_at DESC
               .orderByDesc(TrainingArticle::getId);          // , id DESC
        
        // 调用Service层的list方法，执行查询并返回结果列表
        return CommonResult.success(trainingArticleService.list(wrapper));  // 返回成功响应和文章列表
    }

    /**
     * 分页查询培训文章列表
     * 
     * 功能说明：
     * 1. 支持分页显示，默认10条/页
     * 2. 支持按标题关键词搜索
     * 3. 支持按分类ID筛选
     * 4. 返回分页对象，包含总数、当前页等信息
     * 5. 按发布时间降序排列
     * 
     * 使用场景：
     * - 后台管理系统的文章列表页面
     * - 大量数据的分页展示
     * - 需要显示总数和页码的场景
     * 
     * 请求方式：GET
     * 请求路径：/training/article/page
     * 
     * 请求示例：
     * GET /training/article/page?pageNo=1&pageSize=10&keyword=糖尿病&categoryId=1
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
     * @param keyword 搜索关键词，匹配文章标题
     * @param categoryId 分类ID，精确匹配
     * @return CommonResult<Page<TrainingArticle>> 分页结果
     */
    // @GetMapping：处理GET请求，映射到/training/article/page路径
    @GetMapping("/page")
    public CommonResult<Page<TrainingArticle>> page(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,      // 页码，默认1
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,  // 每页数量，默认10
            @RequestParam(value = "keyword", required = false) String keyword,    // 关键词，可选
            @RequestParam(value = "categoryId", required = false) Long categoryId) {  // 分类ID，可选
        // 创建分页对象，指定当前页码和每页数量
        Page<TrainingArticle> page = new Page<>(pageNo, pageSize);  // MyBatis-Plus的Page对象
        
        // 创建Lambda查询构造器，构造查询条件
        LambdaQueryWrapper<TrainingArticle> wrapper = new LambdaQueryWrapper<>();  // 实例化查询构造器
        
        // 判断关键词是否为空
        if (keyword != null && !keyword.isEmpty()) {  // 关键词不为空时添加搜索条件
            // 添加模糊搜索：标题 LIKE '%keyword%'
            wrapper.like(TrainingArticle::getTitle, keyword);  // 标题模糊匹配
        }
        // 判断分类ID是否为空
        if (categoryId != null) {  // 分类ID不为空时添加筛选条件
            // 添加精确匹配条件：AND category_id = #{categoryId}
            wrapper.eq(TrainingArticle::getCategoryId, categoryId);  // 分类精确匹配
        }
        // 添加排序条件：先按发布时间降序，再按ID降序
        wrapper.orderByDesc(TrainingArticle::getPublishAt)  // ORDER BY publish_at DESC
               .orderByDesc(TrainingArticle::getId);          // , id DESC
        
        // 调用Service层的page方法，执行分页查询
        trainingArticleService.page(page, wrapper);  // 自动填充page对象的records和total属性
        
        // 返回分页结果
        return CommonResult.success(page);  // 返回成功响应和分页数据
    }
}  // TrainingArticleController类结束




