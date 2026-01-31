-- 为项目表添加排序号字段
-- 用于控制项目的显示顺序

ALTER TABLE `projects` 
ADD COLUMN `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号' AFTER `status`;

-- 添加索引以提高查询性能
ALTER TABLE `projects` 
ADD INDEX `idx_sort_order` (`sort_order`);

-- 更新现有数据：将sort_order设置为id值，保持原有顺序
UPDATE `projects` SET `sort_order` = `id` WHERE `sort_order` = 0;
