package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 公寓楼 实体类 */
@Entity
@Table
public class Apartment 
{
	@Id
	@Column(length = 32)
	private String id; //公寓楼ID
	@Column(length = 64)
	private String name; //公寓楼名称
	@Column(length = 128)
	private String address; //公寓楼地址
	@Column
	private int capacity; //入住人数容量
	@Column
	private int usedCapacity; //已使用人数容量
	@Column
	private long createTime; //公寓楼添加时间 时间戳格式
	
	public Apartment() //空构造方法，必须有，否则无法注入
	{
		
	}
	
	public Apartment(String apartmentName)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(12, 20); //对当前nano time进行MD5摘要并取中间8位得出ID
		createTime = System.currentTimeMillis(); //创建时间
		name = apartmentName; //公寓名称
		capacity = 0;
		usedCapacity = 0;
	}
	
	public Apartment(String apartmentName,String apartmentAddr)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(12, 20); //对当前nano time进行MD5摘要并取中间8位得出ID
		createTime = System.currentTimeMillis(); //创建时间
		name = apartmentName; //公寓名称
		address = apartmentAddr;
	}

	public void capacityAdd(int addCapacity) //添加容量
	{
		this.capacity += addCapacity;
	}
	
	public void capacitySub(int subCapacity) //减少容量
	{
		this.capacity -= subCapacity;
	}
	
	public void usedCapAdd(int usedCapAdd) //添加已使用容量
	{
		this.usedCapacity += usedCapAdd;
	}
	
	public void usedCapSub(int usedCapSub) //减少已使用容量
	{
		this.usedCapacity -= usedCapSub;
	}
	
	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getUsedCapacity() {
		return usedCapacity;
	}

	public void setUsedCapacity(int usedCapacity) {
		this.usedCapacity = usedCapacity;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
