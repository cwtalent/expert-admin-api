package com.expert.admin.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {
    
    /**
     * 生成项目导入模板
     * @param projectTypeNames 项目类型名称列表
     */
    public static Resource generateProjectTemplate(List<String> projectTypeNames) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("项目导入模板");
        
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "项目类型", "排序号", "项目名称", "申报人姓名", "申报部门", "主要完成人员", 
            "主管总工", "初评专家1", "初评等级1", "初评专家2", "初评等级2", "状态"
        };
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 创建示例数据行
        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue(projectTypeNames.isEmpty() ? "科技进步奖" : projectTypeNames.get(0));
        exampleRow.createCell(1).setCellValue(1); // 排序号
        exampleRow.createCell(2).setCellValue("示例项目名称");
        exampleRow.createCell(3).setCellValue("张三");
        exampleRow.createCell(4).setCellValue("技术部");
        exampleRow.createCell(5).setCellValue("李四,王五");
        exampleRow.createCell(6).setCellValue("赵六");
        exampleRow.createCell(7).setCellValue("专家A");
        exampleRow.createCell(8).setCellValue("一等");
        exampleRow.createCell(9).setCellValue("专家B");
        exampleRow.createCell(10).setCellValue("二等");
        exampleRow.createCell(11).setCellValue("已批准");
        
        // 创建数据验证（下拉框）
        XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);
        
        // 项目类型下拉框（第0列，从第2行开始，最多1000行）
        if (!projectTypeNames.isEmpty()) {
            String[] projectTypeArray = projectTypeNames.toArray(new String[0]);
            XSSFDataValidationConstraint projectTypeConstraint = 
                (XSSFDataValidationConstraint) validationHelper.createExplicitListConstraint(projectTypeArray);
            CellRangeAddressList projectTypeAddressList = new CellRangeAddressList(1, 1000, 0, 0);
            XSSFDataValidation projectTypeValidation = 
                (XSSFDataValidation) validationHelper.createValidation(projectTypeConstraint, projectTypeAddressList);
            projectTypeValidation.setSuppressDropDownArrow(true);
            projectTypeValidation.setShowErrorBox(true);
            sheet.addValidationData(projectTypeValidation);
        }
        
        // 状态下拉框（第11列，因为增加了排序号列）
        String[] statusOptions = {"草稿", "已提交", "已批准", "已拒绝"};
        XSSFDataValidationConstraint statusConstraint = 
            (XSSFDataValidationConstraint) validationHelper.createExplicitListConstraint(statusOptions);
        CellRangeAddressList statusAddressList = new CellRangeAddressList(1, 1000, 11, 11);
        XSSFDataValidation statusValidation = 
            (XSSFDataValidation) validationHelper.createValidation(statusConstraint, statusAddressList);
        statusValidation.setSuppressDropDownArrow(true);
        statusValidation.setShowErrorBox(true);
        sheet.addValidationData(statusValidation);
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return new ByteArrayResource(outputStream.toByteArray());
    }
    
    /**
     * 生成专家导入模板
     */
    public static Resource generateExpertTemplate() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("专家导入模板");
        
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"姓名", "手机号", "是否启用"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 创建示例数据行
        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue("张三");
        exampleRow.createCell(1).setCellValue("13800138000"); // 手机号可以为空
        exampleRow.createCell(2).setCellValue("是");
        
        // 创建数据验证（下拉框）
        XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);
        
        // 是否启用下拉框（第2列）
        String[] enableOptions = {"是", "否"};
        XSSFDataValidationConstraint enableConstraint = 
            (XSSFDataValidationConstraint) validationHelper.createExplicitListConstraint(enableOptions);
        CellRangeAddressList enableAddressList = new CellRangeAddressList(1, 1000, 2, 2);
        XSSFDataValidation enableValidation = 
            (XSSFDataValidation) validationHelper.createValidation(enableConstraint, enableAddressList);
        enableValidation.setSuppressDropDownArrow(true);
        enableValidation.setShowErrorBox(true);
        sheet.addValidationData(enableValidation);
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return new ByteArrayResource(outputStream.toByteArray());
    }
}
