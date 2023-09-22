package com.questionpro.fileformatgenerator.entity;

public class Student {
    private Integer studentId;
    private String name;
    private String standard;
    private Integer rollNo;
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public Integer getRollNo() {
		return rollNo;
	}
	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}
	public Student(Integer studentId, String name, String standard, Integer rollNo) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.standard = standard;
		this.rollNo = rollNo;
	}
	
    
    
}

