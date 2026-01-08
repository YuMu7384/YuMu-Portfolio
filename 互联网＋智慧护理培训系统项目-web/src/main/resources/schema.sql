-- 智慧护理培训系统 - 数据库自动初始化脚本
-- 此脚本会在Spring Boot启动时自动执行

-- 尝试重命名 name 列为 username（如果还没改）
ALTER TABLE `user` CHANGE COLUMN `name` `username` varchar(64);

-- 备用方案：添加 username 列（如果上面失败）
ALTER TABLE `user` ADD COLUMN `username` varchar(64);
UPDATE `user` SET `username` = `name` WHERE `username` IS NULL;
ALTER TABLE `user` DROP COLUMN `name`;

-- 为 user 表添加新字段（安全方式，检查字段是否存在）
SET @sql1 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'avatar') = 0, 
    'ALTER TABLE `user` ADD COLUMN `avatar` varchar(512) COMMENT ''头像URL''', 'SELECT 1');
PREPARE stmt1 FROM @sql1;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;

SET @sql2 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'phone') = 0, 
    'ALTER TABLE `user` ADD COLUMN `phone` varchar(32) COMMENT ''联系电话''', 'SELECT 1');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

SET @sql3 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'address') = 0, 
    'ALTER TABLE `user` ADD COLUMN `address` varchar(256) COMMENT ''地址''', 'SELECT 1');
PREPARE stmt3 FROM @sql3;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;

SET @sql4 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'department') = 0, 
    'ALTER TABLE `user` ADD COLUMN `department` varchar(128) COMMENT ''部门''', 'SELECT 1');
PREPARE stmt4 FROM @sql4;
EXECUTE stmt4;
DEALLOCATE PREPARE stmt4;

SET @sql5 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'position') = 0, 
    'ALTER TABLE `user` ADD COLUMN `position` varchar(128) COMMENT ''职位''', 'SELECT 1');
PREPARE stmt5 FROM @sql5;
EXECUTE stmt5;
DEALLOCATE PREPARE stmt5;

SET @sql6 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'created_at') = 0, 
    'ALTER TABLE `user` ADD COLUMN `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间''', 'SELECT 1');
PREPARE stmt6 FROM @sql6;
EXECUTE stmt6;
DEALLOCATE PREPARE stmt6;

SET @sql7 = IF((SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'updated_at') = 0, 
    'ALTER TABLE `user` ADD COLUMN `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''更新时间''', 'SELECT 1');
PREPARE stmt7 FROM @sql7;
EXECUTE stmt7;
DEALLOCATE PREPARE stmt7;

-- 初始化默认管理员账号（如果不存在）
INSERT INTO `user` (username, age, email, password, avatar, phone, address, department, position)
SELECT 'admin', 28, 'admin@example.com', '123456', 
       'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
       '138-0000-0000',
       '北京市朝阳区某某街道',
       '护理部',
       '护士长'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE username = 'admin');

-- 自动创建培训相关表（如果不存在）

-- 培训类别表
CREATE TABLE IF NOT EXISTS `training_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `name` varchar(128) DEFAULT NULL COMMENT '类别名称',
  `description` varchar(512) DEFAULT NULL COMMENT '类别描述',
  `sort_order` int DEFAULT 0 COMMENT '排序序号',
  `status` tinyint DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训类别表';

-- 培训标签表
CREATE TABLE IF NOT EXISTS `training_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` varchar(128) DEFAULT NULL COMMENT '标签名称',
  `status` tinyint DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训标签表';

-- 培训文章表
CREATE TABLE IF NOT EXISTS `training_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(256) DEFAULT NULL COMMENT '文章标题',
  `cover_url` varchar(512) DEFAULT NULL COMMENT '封面图片URL',
  `category_id` bigint DEFAULT NULL COMMENT '所属类别ID',
  `tag_ids` varchar(512) DEFAULT NULL COMMENT '标签ID列表，逗号分隔',
  `content` mediumtext COMMENT '文章内容',
  `publish_status` tinyint DEFAULT 0 COMMENT '发布状态：0草稿 1已发布',
  `publish_at` datetime DEFAULT NULL COMMENT '发布时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训文章表';

-- 培训视频表
CREATE TABLE IF NOT EXISTS `training_video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '视频ID',
  `title` varchar(256) DEFAULT NULL COMMENT '视频标题',
  `cover_url` varchar(512) DEFAULT NULL COMMENT '视频封面URL',
  `video_url` varchar(1024) DEFAULT NULL COMMENT '视频文件URL',
  `duration_seconds` int DEFAULT NULL COMMENT '视频时长（秒）',
  `category_id` bigint DEFAULT NULL COMMENT '所属类别ID',
  `tag_ids` varchar(512) DEFAULT NULL COMMENT '标签ID列表，逗号分隔',
  `publish_status` tinyint DEFAULT 0 COMMENT '发布状态：0草稿 1已发布',
  `publish_at` datetime DEFAULT NULL COMMENT '发布时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训视频表';

-- 培训PPT表
CREATE TABLE IF NOT EXISTS `training_ppt` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'PPT ID',
  `title` varchar(256) DEFAULT NULL COMMENT 'PPT标题',
  `cover_url` varchar(512) DEFAULT NULL COMMENT 'PPT封面URL',
  `file_url` varchar(1024) DEFAULT NULL COMMENT 'PPT文件URL',
  `pages` int DEFAULT NULL COMMENT 'PPT页数',
  `category_id` bigint DEFAULT NULL COMMENT '所属类别ID',
  `tag_ids` varchar(512) DEFAULT NULL COMMENT '标签ID列表，逗号分隔',
  `publish_status` tinyint DEFAULT 0 COMMENT '发布状态：0草稿 1已发布',
  `publish_at` datetime DEFAULT NULL COMMENT '发布时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训PPT表';
