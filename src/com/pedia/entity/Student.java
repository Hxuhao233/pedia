package com.pedia.entity;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
	private Integer id;

	private Integer universityid;

	private String account;

	private String password;

	private String name;

	private String studentnum;

	private Date birthdate;

	private String iconaddr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUniversityid() {
		return universityid;
	}

	public void setUniversityid(Integer universityid) {
		this.universityid = universityid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account == null ? null : account.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getStudentnum() {
		return studentnum;
	}

	public void setStudentnum(String studentnum) {
		this.studentnum = studentnum == null ? null : studentnum.trim();
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getIconaddr() {
		return iconaddr;
	}

	public void setIconaddr(String iconaddr) {
		this.iconaddr = iconaddr == null ? null : iconaddr.trim();
	}

	@Override
	public String toString() {
		return "Student : " + account + password + name;

	}
}