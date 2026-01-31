-- 创建获奖表
-- 用于存储最终获奖结果

CREATE TABLE IF NOT EXISTS `awards` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `session_guid` VARCHAR(36) NOT NULL COMMENT '投票会话GUID',
  `project_type_id` INT NOT NULL COMMENT '项目类型ID',
  `project_id` INT NOT NULL COMMENT '项目ID',
  `award_level` VARCHAR(20) NOT NULL COMMENT '获奖等级: 1st/2nd/3rd',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_project_level` (`session_guid`, `project_id`, `award_level`),
  KEY `idx_session_guid` (`session_guid`),
  KEY `idx_project_type_id` (`project_type_id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_award_level` (`award_level`),
  CONSTRAINT `fk_awards_project_type` FOREIGN KEY (`project_type_id`) REFERENCES `project_types` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_awards_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='获奖表';
