package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExpertLoginLogDto {
    private Integer id;
    private Integer expertId;
    private String expertName;  // 专家姓名
    private LocalDateTime loginTime;
    private String ipAddress;
    private String userAgent;
    private String device;  // 设备类型（根据UA判断）
    private String loginStatus;
    private String failureReason;
    private LocalDateTime createdAt;
}
