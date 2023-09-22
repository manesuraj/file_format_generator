package com.questionpro.fileformatgenerator.service;

import com.questionpro.fileformatgenerator.entity.Student;
import com.questionpro.fileformatgenerator.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    private ExcelUtil getStudentList() {
        List<Student> accountReport = Arrays.asList(
                new Student(1, "Suraj", "BE", 11),
                new Student(1, "Rushi", "BE", 22)
        );
        return new ExcelUtil(accountReport);
    }

    public void exportToExcel(HttpServletResponse response) {
        ExcelUtil excelUtil = getStudentList();
        excelUtil.exportToExcel(response);
    }

    public byte[] generateCsvFromExcel() throws IOException {
        ExcelUtil excelUtil = getStudentList();
        return excelUtil.generateCsvFromExcel();
    }
}
