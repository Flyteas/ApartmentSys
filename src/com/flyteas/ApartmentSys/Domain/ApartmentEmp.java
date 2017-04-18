package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 公寓职员 实体类 */
@Entity
@Table
public class ApartmentEmp 
{
	@Id
	@Column(length = 32)
	private String id; //ID
	@ManyToOne
	private Employee employee; //对应的职员
	@ManyToOne
	private Apartment apartment; //对应的公寓楼
	@Column
	private long createTime; //创建时间
	
	public ApartmentEmp()
	{
		
	}
	
	public ApartmentEmp(Employee employee,Apartment apartment)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(8, 24); //对当前nano time进行MD5摘要并取中间16位得出ID
		createTime = System.currentTimeMillis(); //创建时间
		this.employee = employee;
		this.apartment = apartment;
	}

	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
