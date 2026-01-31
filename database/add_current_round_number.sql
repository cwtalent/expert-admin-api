-- 添加 current_round_number 字段到 voting_sessions 表
ALTER TABLE voting_sessions
ADD COLUMN current_round_number INT NOT NULL DEFAULT 1 COMMENT '当前投票轮次' AFTER status;
