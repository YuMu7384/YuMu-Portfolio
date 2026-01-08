/*
 智慧护理培训系统 - 数据库初始化脚本
 
 系统名称：互联网+智慧护理培训系统
 功能模块：
 - 用户管理
 - 培训类别管理
 - 培训标签管理
 - 培训文章管理
 - 培训视频管理
 - 培训PPT管理
 
 Date: 2025-10-30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户id索引',
  `username` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户名',
  `age` int(0) DEFAULT NULL COMMENT '年龄',
  `email` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '密码',
  `avatar` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `address` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `department` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '部门',
  `position` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '职位',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 初始化默认管理员账号
-- ----------------------------
INSERT INTO `user` (`username`, `age`, `email`, `password`, `avatar`, `phone`, `address`, `department`, `position`) 
VALUES ('admin', 28, 'admin@example.com', '123456', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', '138-0000-0000', '北京市朝阳区某某街道', '护理部', '护士长');

-- ----------------------------
-- Table structure for training_category
-- ----------------------------
DROP TABLE IF EXISTS `training_category`;
CREATE TABLE `training_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `sort_order` int DEFAULT 0,
  `status` tinyint DEFAULT 1,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for training_tag
-- ----------------------------
DROP TABLE IF EXISTS `training_tag`;
CREATE TABLE `training_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `status` tinyint DEFAULT 1,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for training_article
-- ----------------------------
DROP TABLE IF EXISTS `training_article`;
CREATE TABLE `training_article` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL,
  `cover_url` varchar(512) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `tag_ids` varchar(512) DEFAULT NULL,
  `content` mediumtext,
  `publish_status` tinyint DEFAULT 0,
  `publish_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for training_video
-- ----------------------------
DROP TABLE IF EXISTS `training_video`;
CREATE TABLE `training_video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL,
  `cover_url` varchar(512) DEFAULT NULL,
  `video_url` varchar(1024) DEFAULT NULL,
  `duration_seconds` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `tag_ids` varchar(512) DEFAULT NULL,
  `publish_status` tinyint DEFAULT 0,
  `publish_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for training_ppt
-- ----------------------------
DROP TABLE IF EXISTS `training_ppt`;
CREATE TABLE `training_ppt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL,
  `cover_url` varchar(512) DEFAULT NULL,
  `file_url` varchar(1024) DEFAULT NULL,
  `pages` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `tag_ids` varchar(512) DEFAULT NULL,
  `publish_status` tinyint DEFAULT 0,
  `publish_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
