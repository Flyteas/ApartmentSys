package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 职员 实体类 */
@Entity
@Table
public class Employee 
{
	@Id
	@Column(length = 32)
	private String empId; //职员工号
	@Column(length = 32)
	private String name; //职员姓名
	@Column
	private int sex; //性别 0男1女
	@Column(length = 32)
	private String phone; //电话
	@Column
	private int state; //职员状态 0在职 1离职
	@Column
	private long inductionTime; //入职时间
	@Column
	private long departureTime; //离职时间
	@Column
	private long createTime; //创建时间
	
	public Employee()
	{
		
	}
	
	public Employee(String empId,String name,int sex,String phone,int state,long indTime,long depTime)
	{
		createTime = System.currentTimeMillis(); //创建时间
		this.empId = empId;
		this.name = name;
		this.sex = sex;
		this.phone = phone;
		this.state = state;
		this.inductionTime = indTime;
		this.departureTime = depTime;
	}

	/* setter和getter */
	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getInductionTime() {
		return inductionTime;
	}

	public void setInductionTime(long inductionTime) {
		this.inductionTime = inductionTime;
	}

	public long getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(long departureTime) {
		this.departureTime = departureTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
