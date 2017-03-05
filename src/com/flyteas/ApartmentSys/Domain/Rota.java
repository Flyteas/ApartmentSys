package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 值班信息 实体类 */
@Entity
@Table
public class Rota 
{
	@Id
	@Column(length = 32)
	private String id; //ID
	@ManyToOne
	private ApartmentEmp apartmentEmp; //对应的公寓职员
	@Column
	private long startTime; //值班开始时间
	@Column
	private long endTime; //值班结束时间
	@Column
	private long createTime; //添加时间 时间戳格式
	
	public Rota()
	{
		
	}
	
	public Rota(ApartmentEmp apartmentEmp,long startTime,long endTime)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())); //对当前nano time进行MD5摘要得出ID
		createTime = System.currentTimeMillis(); //创建时间
		this.apartmentEmp = apartmentEmp;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ApartmentEmp getApartmentEmp() {
		return apartmentEmp;
	}

	public void setApartmentEmp(ApartmentEmp apartmentEmp) {
		this.apartmentEmp = apartmentEmp;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
