-- 修改专家表，将手机号字段改为可空
-- 执行此脚本以更新现有数据库表结构

USE `expert`;

-- 步骤1: 删除现有的唯一索引
-- 注意：如果索引不存在会报错，可以忽略
ALTER TABLE `experts` DROP INDEX `uk_phone`;

-- 步骤2: 修改 phone 字段为可空
ALTER TABLE `experts` 
MODIFY COLUMN `phone` VARCHAR(20) NULL COMMENT '手机号（可选）';

-- 步骤3: 重新创建唯一索引（MySQL 允许多个 NULL 值，但非 NULL 值必须唯一）
CREATE UNIQUE INDEX `uk_phone` ON `experts` (`phone`);
