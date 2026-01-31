package com.expert.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.expert.admin.dto.ExpertDto;
import com.expert.admin.dto.ExpertSearchDto;
import com.expert.admin.dto.PageResult;
import com.expert.admin.entity.Expert;
import com.expert.admin.mapper.ExpertMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpertService {
    
    @Autowired
    private ExpertMapper mapper;
    
    public List<ExpertDto> findAll() {
        QueryWrapper<Expert> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return mapper.selectList(queryWrapper).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public PageResult<ExpertDto> search(ExpertSearchDto searchDto) {
        Page<Expert> page = new Page<>(searchDto.getPage(), searchDto.getPageSize());
        
        // 将空字符串转换为 null，避免查询问题
        String name = (searchDto.getName() != null && !searchDto.getName().trim().isEmpty()) 
            ? searchDto.getName().trim() : null;
        String phone = (searchDto.getPhone() != null && !searchDto.getPhone().trim().isEmpty()) 
            ? searchDto.getPhone().trim() : null;
        
        IPage<Expert> result = mapper.searchExperts(
            page,
            name,
            phone,
            searchDto.getIsActive()
        );
        
        List<ExpertDto> content = result.getRecords().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageResult<>(
            content,
            result.getTotal(),
            searchDto.getPage(),
            searchDto.getPageSize()
        );
    }
    
    public void batchDelete(List<Integer> ids) {
        mapper.deleteBatchIds(ids);
    }
    
    public ExpertDto findById(Integer id) {
        Expert entity = mapper.selectById(id);
        if (entity == null) {
            return null;
        }
        return toDto(entity);
    }
    
    public ExpertDto findByPhone(String phone) {
        Expert entity = mapper.findByPhone(phone);
        if (entity == null) {
            return null;
        }
        return toDto(entity);
    }
    
    public ExpertDto create(ExpertDto dto) {
        // 如果提供了手机号，检查是否已存在
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            Expert existing = mapper.findByPhone(dto.getPhone());
            if (existing != null) {
                throw new RuntimeException("手机号已存在");
            }
        }
        
        Expert entity = new Expert();
        BeanUtils.copyProperties(dto, entity);
        mapper.insert(entity);
        return toDto(entity);
    }
    
    public ExpertDto update(Integer id, ExpertDto dto) {
        Expert entity = mapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("专家不存在");
        }
        
        // 检查手机号是否被其他专家使用（仅当提供了手机号时）
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            Expert existing = mapper.findByPhone(dto.getPhone());
            if (existing != null && !existing.getId().equals(id)) {
                throw new RuntimeException("手机号已被其他专家使用");
            }
        }
        
        BeanUtils.copyProperties(dto, entity, "id", "createdAt");
        mapper.updateById(entity);
        return toDto(entity);
    }
    
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
    
    public void batchImport(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            // 从第2行开始读取（第1行是标题，第2行是示例数据）
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    // 读取姓名（第0列）
                    Cell nameCell = row.getCell(0);
                    if (nameCell == null) continue;
                    String name = getCellValueAsString(nameCell);
                    if (name == null || name.trim().isEmpty()) {
                        throw new RuntimeException("姓名不能为空");
                    }
                    
                    // 读取手机号（第1列，可选）
                    Cell phoneCell = row.getCell(1);
                    String phone = getCellValueAsString(phoneCell);
                    // 处理空值：空字符串、null、空白字符都转换为 null
                    if (phone == null || phone.trim().isEmpty()) {
                        phone = null;
                    } else {
                        phone = phone.trim();
                        // 如果提供了手机号，检查是否已存在
                        Expert existing = mapper.findByPhone(phone);
                        if (existing != null) {
                            throw new RuntimeException("手机号已存在: " + phone);
                        }
                    }
                    
                    // 读取是否启用（第2列）
                    Cell enableCell = row.getCell(2);
                    String enableChinese = getCellValueAsString(enableCell);
                    Boolean isActive = true; // 默认值
                    if (enableChinese != null && !enableChinese.trim().isEmpty()) {
                        String enable = enableChinese.trim();
                        if ("否".equals(enable) || "false".equalsIgnoreCase(enable) || "0".equals(enable)) {
                            isActive = false;
                        } else if ("是".equals(enable) || "true".equalsIgnoreCase(enable) || "1".equals(enable)) {
                            isActive = true;
                        }
                    }
                    
                    // 创建专家DTO
                    ExpertDto dto = new ExpertDto();
                    dto.setName(name.trim());
                    dto.setPhone(phone); // phone 已经是处理过的值（null 或 trimmed 字符串）
                    dto.setIsActive(isActive);
                    
                    // 保存专家
                    create(dto);
                    successCount++;
                    
                } catch (Exception e) {
                    failCount++;
                    errorMessages.append("第").append(i + 1).append("行: ").append(e.getMessage()).append("\n");
                }
            }
            
            workbook.close();
            inputStream.close();
            
            if (failCount > 0) {
                throw new RuntimeException(String.format("导入完成，成功: %d 条，失败: %d 条\n%s", 
                    successCount, failCount, errorMessages.toString()));
            }
            
        } catch (Exception e) {
            throw new RuntimeException("导入失败: " + e.getMessage(), e);
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字，避免科学计数法
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private ExpertDto toDto(Expert entity) {
        ExpertDto dto = new ExpertDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
