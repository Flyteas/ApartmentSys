package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 学生 实体类 */
@Entity
@Table
public class Student 
{
	@Id
	@Column(length = 32)
	private String stuId; //学生学号
	@Column(length = 32)
	private String name; //学生姓名
	@Column
	private int sex; //学生性别 0为男 1为女
	@Column(length = 32)
	private String phone; //联系电话
	@Column
	private long createTime; //添加时间 时间戳格式
	
	public Student() //空构造方法，必须有，否则无法注入
	{
		
	}
	
	public Student(String stuId,String name,int sex,String phone)
	{
		createTime = System.currentTimeMillis(); //创建时间
		this.stuId = stuId;
		this.name = name;
		this.sex = sex;
		this.phone = phone;
	}

	/* setter和getter */
	public String getStuId() 
	{
		return stuId;
	}

	public void setStuId(String stuId) 
	{
		this.stuId = stuId;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getSex() 
	{
		return sex;
	}

	public void setSex(int sex) 
	{
		this.sex = sex;
	}

	public String getPhone() 
	{
		return phone;
	}

	public void setPhone(String phone) 
	{
		this.phone = phone;
	}

	public long getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(long createTime) 
	{
		this.createTime = createTime;
	}
}
