package com.hwadee.mybatisplustest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 智慧护理培训系统 - 应用程序主入口
 * 
 * 功能说明:
 * 1. Spring Boot 应用程序的启动类
 * 2. 配置MyBatis-Plus的Mapper扫描路径
 * 3. 启用Spring事务管理功能
 * 
 * 注解说明:
 * @SpringBootApplication - Spring Boot核心注解,包含:
 *   - @Configuration: 声明这是一个配置类
 *   - @EnableAutoConfiguration: 启用自动配置
 *   - @ComponentScan: 自动扫描组件
 * 
 * @EnableTransactionManagement - 启用Spring声明式事务管理
 *   作用: 允许在Service层使用@Transactional注解来管理数据库事务
 * 
 * @MapperScan - 指定MyBatis-Plus的Mapper接口扫描路径
 *   作用: 自动将指定包下的Mapper接口注册为Spring Bean
 *   路径: com.hwadee.mybatisplustest.mapper
 * 
 * @author AI Assistant
 * @version 1.6.0
 * @since 2025-10-21
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.hwadee.mybatisplustest.mapper")
public class MybatisPlusTestApplication {

    /**
     * 应用程序主方法 - 程序启动入口
     * 
     * @param args 命令行参数
     * 
     * 启动流程:
     * 1. 初始化Spring容器
     * 2. 加载application.yml配置文件
     * 3. 自动配置数据源、MyBatis-Plus等组件
     * 4. 扫描并注册所有Controller、Service、Mapper
     * 5. 启动内嵌Tomcat服务器(默认端口8080)
     */
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusTestApplication.class, args);
    }
}
