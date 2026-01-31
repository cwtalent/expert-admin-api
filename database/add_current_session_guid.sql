-- 添加 current_session_guid 字段到 project_types 表
ALTER TABLE project_types
ADD COLUMN current_session_guid VARCHAR(36) NULL COMMENT '当前投票会话GUID' AFTER third_prize_count;
