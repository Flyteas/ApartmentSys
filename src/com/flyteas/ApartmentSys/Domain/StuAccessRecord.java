package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 学生门禁记录 实体类 */
@Entity
@Table
public class StuAccessRecord 
{
	@Id
	@Column(length = 32)
	private String id; //记录ID
	@Column
	private int type; //门禁出入类型，0为进入公寓楼，1为离开公寓楼
	@ManyToOne
	private Student student; //对应的学生
	@ManyToOne
	private Apartment apartment; //对应的公寓楼
	@Column
	private long accessTime; //门禁记录时间 时间戳格式
	
	public StuAccessRecord() //空构造方法，必须有，否则无法注入
	{
		
	}
	
	public StuAccessRecord(Student student,Apartment apartment,int type)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(8, 24); //对当前nano time进行MD5摘要并取中间16位得出ID
		accessTime = System.currentTimeMillis(); //门禁记录时间
		this.student = student;
		this.apartment = apartment;
		this.type = type;
	}

	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
