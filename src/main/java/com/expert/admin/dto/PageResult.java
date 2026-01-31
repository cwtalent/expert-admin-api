package com.expert.admin.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    
    public PageResult(List<T> content, long totalElements, int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }
}
