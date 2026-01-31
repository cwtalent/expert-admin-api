package com.expert.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectTypeDto {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;
    private Integer firstPrizeCount;
    private Integer secondPrizeCount;
    private Integer thirdPrizeCount;
    private String currentSessionGuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
