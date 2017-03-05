package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 管理员 实体类 */
@Entity
@Table
public class Manager 
{
	@Id
	@Column(length = 32)
	private String username; //用户名
	@Column(length = 32)
	private String password; //密码  MD5加salt加密
	@Column(length = 32)
	private String realName; //真实姓名
	@Column
	private int sex; //性别 0男1女
	@Column(length = 32)
	private String phone; //联系电话
	@Column
	private long createTime; //创建时间
	@Column(nullable = false,columnDefinition = "bigint default 0")
	private long lastLoginTime; //上一次登陆时间，时间戳形式(ms)
	@Column(length = 16)
	private String lastLoginIP; //上一次登陆IP
	
	public Manager()
	{
		
	}
	
	public Manager(String username,String password,String realName,int sex,String phone)
	{
		createTime = System.currentTimeMillis(); //创建时间
		this.username = username;
		this.password = password;
		this.realName = realName;
		this.sex = sex;
		this.phone = phone;
	}

	/* setter和getter */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
}
