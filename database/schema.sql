-- 投票系统后台管理数据库结构
-- 数据库: expert
-- MySQL 8.0+

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `expert` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `expert`;

-- 1. 项目类型表
CREATE TABLE IF NOT EXISTS `project_types` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '类型名称',
  `code` VARCHAR(20) NOT NULL COMMENT '类型代码',
  `description` TEXT NULL COMMENT '描述',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `first_prize_count` INT NULL COMMENT '一等奖数量',
  `second_prize_count` INT NULL COMMENT '二等奖数量',
  `third_prize_count` INT NULL COMMENT '三等奖数量',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目类型表';

-- 2. 专家表
CREATE TABLE IF NOT EXISTS `experts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) NULL COMMENT '手机号（可选）',
  `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `last_login_at` DATETIME NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家表';

-- 3. 项目表
CREATE TABLE IF NOT EXISTS `projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `project_type_id` INT NOT NULL COMMENT '项目类型ID',
  `applicant_name` VARCHAR(100) NULL COMMENT '申报人姓名',
  `applicant_department` VARCHAR(200) NULL COMMENT '申报部门',
  `project_name` VARCHAR(500) NOT NULL COMMENT '项目或课题名称',
  `main_completers` TEXT NULL COMMENT '主要完成人员（JSON格式或逗号分隔）',
  `chief_engineer` VARCHAR(100) NULL COMMENT '主管总工姓名',
  `preliminary_expert1_name` VARCHAR(100) NULL COMMENT '初评专家1姓名',
  `preliminary_level1` VARCHAR(20) NULL COMMENT '初评等级1',
  `preliminary_expert2_name` VARCHAR(100) NULL COMMENT '初评专家2姓名',
  `preliminary_level2` VARCHAR(20) NULL COMMENT '初评等级2',
  `status` VARCHAR(20) NOT NULL DEFAULT 'draft' COMMENT '状态: draft/submitted/approved/rejected',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_project_type_id` (`project_type_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_projects_project_type` FOREIGN KEY (`project_type_id`) REFERENCES `project_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- 4. 专家登录日志表
CREATE TABLE IF NOT EXISTS `expert_login_logs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `expert_id` INT NOT NULL COMMENT '专家ID',
  `login_time` DATETIME NOT NULL COMMENT '登录时间',
  `ip_address` VARCHAR(50) NULL COMMENT 'IP地址',
  `user_agent` TEXT NULL COMMENT '浏览器UA',
  `login_status` VARCHAR(20) NOT NULL DEFAULT 'success' COMMENT '登录状态: success/failed',
  `failure_reason` VARCHAR(200) NULL COMMENT '失败原因',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_expert_id` (`expert_id`),
  KEY `idx_login_time` (`login_time`),
  KEY `idx_login_status` (`login_status`),
  CONSTRAINT `fk_login_logs_expert` FOREIGN KEY (`expert_id`) REFERENCES `experts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家登录日志表';

-- 5. 投票会话表
CREATE TABLE IF NOT EXISTS `voting_sessions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `session_guid` VARCHAR(36) NOT NULL COMMENT '唯一GUID，用于二维码',
  `project_type_id` INT NOT NULL COMMENT '项目类型ID',
  `qr_code_url` VARCHAR(500) NULL COMMENT '二维码图片URL或base64',
  `voting_url` VARCHAR(500) NOT NULL COMMENT '投票页面URL',
  `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/active/completed/closed',
  `started_at` DATETIME NULL COMMENT '开始时间',
  `closed_at` DATETIME NULL COMMENT '结束时间',
  `created_by` VARCHAR(100) NULL COMMENT '创建人',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_guid` (`session_guid`),
  KEY `idx_project_type_id` (`project_type_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_voting_sessions_project_type` FOREIGN KEY (`project_type_id`) REFERENCES `project_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票会话表';

-- 6. 投票表
CREATE TABLE IF NOT EXISTS `votes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `session_guid` VARCHAR(36) NOT NULL COMMENT '投票会话GUID',
  `project_type_id` INT NOT NULL COMMENT '项目类型ID',
  `round_number` INT NOT NULL DEFAULT 1 COMMENT '轮次，1=第一轮，2=第二轮，以此类推',
  `project_id` INT NOT NULL COMMENT '项目ID',
  `expert_id` INT NOT NULL COMMENT '专家ID',
  `award_level` VARCHAR(20) NOT NULL COMMENT '投票奖级: 1st/2nd/3rd/reject',
  `vote_time` DATETIME NOT NULL COMMENT '投票时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vote` (`session_guid`, `round_number`, `project_id`, `expert_id`),
  KEY `idx_session_guid` (`session_guid`),
  KEY `idx_project_type_id` (`project_type_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_expert_id` (`expert_id`),
  KEY `idx_round_number` (`round_number`),
  CONSTRAINT `fk_votes_project_type` FOREIGN KEY (`project_type_id`) REFERENCES `project_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_votes_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_votes_expert` FOREIGN KEY (`expert_id`) REFERENCES `experts` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票表';

-- 插入初始项目类型数据
INSERT INTO `project_types` (`name`, `code`, `description`, `sort_order`, `is_active`) VALUES
('科技进步奖', 'tech_progress', '科技进步奖', 1, 1),
('优秀设计奖', 'design', '优秀设计奖', 2, 1),
('优秀勘测奖', 'survey', '优秀勘测奖', 3, 1),
('优秀软件奖', 'software', '优秀软件奖', 4, 1),
('优秀咨询奖', 'consulting', '优秀咨询奖', 5, 1)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
