package com.questionpro.fileformatgenerator.util;

import com.questionpro.fileformatgenerator.entity.Student;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExcelUtil {
    private final XSSFWorkbook workbook;
    List<Student> studentList;
    private XSSFSheet sheet;

    public ExcelUtil(List<Student> beans) {
        this.studentList = beans;
        workbook = new XSSFWorkbook();
    }

    public void createCell(Row row, int columnCounter, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCounter);
        Cell cell = row.createCell(columnCounter);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    public void createHeaderRow() {
        sheet = workbook.createSheet("Student Data");
        CellStyle style = workbook.createCellStyle();
        Row row = sheet.createRow(0);
        XSSFFont font1 = workbook.createFont();
        font1.setBold(true);
        font1.setFontHeight(12);
        style.setFont(font1);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        createCell(row, 0, "STUDENT ID", style);
        createCell(row, 1, "STUDENT NAME", style);
        createCell(row, 2, "STANDARD", style);
        createCell(row, 3, "ROLL NO", style);
    }

    public void writeExcel() {
        int rowCounter = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        for (Student bean : studentList) {
            Row row = sheet.createRow(rowCounter++);
            int columnCounter = 0;
            createCell(row, columnCounter++, bean.getStudentId(), style);
            createCell(row, columnCounter++, bean.getName(), style);
            createCell(row, columnCounter++, bean.getStandard(), style);
            createCell(row, columnCounter++, bean.getRollNo(), style);
        }
    }

    public void createSpreadsheet() {
        createHeaderRow();
        writeExcel();
    }

    public void exportToExcel(HttpServletResponse response) {
        createSpreadsheet();
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] generateCsvFromExcel() throws IOException {
        createSpreadsheet();
        // Create an in-memory output stream for the CSV data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = null;

        try {
            // Create a CSVPrinter with the desired CSV format
            csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT);
            // Iterate through the Excel rows and write them to the CSV
            for (Row row : workbook.getSheetAt(0)) {
                Object[] rowData = new Object[row.getLastCellNum()];
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        rowData[i] = cell.toString();
                        System.out.println("#### data " + rowData[i]);
                    } else {
                        rowData[i] = "";
                    }
                }
                csvPrinter.printRecord(rowData);
            }
        } finally {
            if (csvPrinter != null) {
                csvPrinter.close();
            }
            workbook.close();
        }

        return outputStream.toByteArray();
    }

}
