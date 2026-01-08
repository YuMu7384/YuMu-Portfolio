package com.hwadee.mybatisplustest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 病人信息实体类
 * 
 * 功能说明:
 * 1. 存储老年病人的完整信息
 * 2. 包含基本信息、住院信息、医疗信息
 * 3. 支持护理等级和病人状态管理
 * 
 * 数据库表名: patient
 * 
 * 字段分类:
 * - 基本信息: 编号、姓名、性别、年龄、身份证、电话
 * - 紧急联系人: 联系人姓名和电话
 * - 住院信息: 房间号、床位号、入院日期、护理等级
 * - 医疗信息: 诊断、病史、过敏史、当前用药
 * - 状态信息: 在院/出院/转院状态
 * 
 * 注解说明:
 * @Data - Lombok注解,自动生成getter/setter/toString等方法
 * @TableName - MyBatis-Plus注解,指定对应的数据库表名
 * 
 * @author AI Assistant
 * @version 1.6.0
 */
@Data
@TableName("patient")
public class Patient {
    /**
     * 主键 ID - 自增长
     * @TableId(type = IdType.AUTO) - 主键策略为数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 病人编号 - 唯一标识
     * 格式: P+年份+流水号 (例: P20250001)
     * @TableField - 映射到patient_no字段
     */
    @TableField("patient_no")
    private String patientNo;
    
    /**
     * 病人姓名
     */
    private String name;
    
    /**
     * 性别
     * 1=男, 2=女
     */
    private Integer gender;
    
    /**
     * 年龄(岁)
     */
    private Integer age;
    
    /**
     * 身份证号
     * 格式: 18位数字
     */
    @TableField("id_card")
    private String idCard;
    
    /**
     * 病人联系电话
     */
    private String phone;
    
    /**
     * 紧急联系人姓名
     * 通常为家属
     */
    @TableField("emergency_contact")
    private String emergencyContact;
    
    /**
     * 紧急联系人电话
     */
    @TableField("emergency_phone")
    private String emergencyPhone;
    
    /**
     * 家庭住址
     */
    private String address;
    
    /**
     * 入院日期
     * 类型: LocalDateTime
     */
    @TableField("admission_date")
    private LocalDateTime admissionDate;
    
    /**
     * 诊断信息
     * 存储病情诊断和病情描述
     */
    private String diagnosis;
    
    /**
     * 病史
     * 包括既往病史、慢性病等
     */
    @TableField("medical_history")
    private String medicalHistory;
    
    /**
     * 过敏史
     * 记录药物过敏、食物过敏等
     */
    @TableField("allergy_history")
    private String allergyHistory;
    
    /**
     * 当前用药
     * 记录正在使用的药物
     */
    @TableField("current_medication")
    private String currentMedication;
    
    /**
     * 护理等级
     * 可选值:
     * - 特级护理: 病情危重需专人24小时护理
     * - 一级护理: 病情较重需加强护理
     * - 二级护理: 病情稳定需定期巡视
     * - 三级护理: 病情轻微可自理
     */
    @TableField("care_level")
    private String careLevel;
    
    /**
     * 房间号
     * 例: 301, 402
     */
    @TableField("room_no")
    private String roomNo;
    
    /**
     * 床位号
     * 例: 1, 2, 3
     */
    @TableField("bed_no")
    private String bedNo;
    
    /**
     * 病人状态
     * 1=在院, 2=出院, 3=转院
     */
    private Integer status;
    
    /**
     * 备注信息
     * 其他需要记录的信息
     */
    private String remarks;
    
    /**
     * 创建人 ID
     * 关联到user表
     */
    @TableField("created_by")
    private Long createdBy;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     * 记录最后一次修改时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}


