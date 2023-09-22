package com.questionpro.fileformatgenerator.controller;

import com.questionpro.fileformatgenerator.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/generate-export")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@GetMapping("/{fileFormat}")
	public ResponseEntity<?> getAccountReport(HttpServletResponse response, @PathVariable String fileFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String currentDate = dateFormat.format(Date.from(Instant.now()));

		if (fileFormat.equalsIgnoreCase("excel")) {
			String fileName = "Student_List" + currentDate + ".xlsx";
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheetl");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + fileName;
			response.setHeader(headerKey, headerValue);
			studentService.exportToExcel(response);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else if (fileFormat.equalsIgnoreCase("csv")) {
			byte[] csvContent;
			try {
				csvContent = studentService.generateCsvFromExcel();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.valueOf("application/csv"));
				headers.setContentDispositionFormData("attachment", "Student_List" + currentDate + ".csv");
				return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return new ResponseEntity<>("Choose valid File Format", HttpStatus.BAD_REQUEST);
		}

	}

}
