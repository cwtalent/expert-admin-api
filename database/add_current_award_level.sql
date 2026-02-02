-- 添加 current_award_level 字段到 voting_sessions 表
ALTER TABLE voting_sessions
ADD COLUMN current_award_level ENUM('1st', '2nd', '3rd') NULL COMMENT '当前投票轮次对应的奖级，NULL表示从一等奖开始' AFTER current_round_number;
