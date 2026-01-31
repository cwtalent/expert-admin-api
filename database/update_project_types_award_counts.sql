-- 为项目类型表添加奖项数量字段
-- 执行此脚本以更新现有数据库表结构

USE `expert`;

-- 添加奖项数量字段
ALTER TABLE `project_types` 
ADD COLUMN `first_prize_count` INT NULL COMMENT '一等奖数量' AFTER `is_active`,
ADD COLUMN `second_prize_count` INT NULL COMMENT '二等奖数量' AFTER `first_prize_count`,
ADD COLUMN `third_prize_count` INT NULL COMMENT '三等奖数量' AFTER `second_prize_count`;
