package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 访客记录 实体类 */
@Entity
@Table
public class VisitRecord 
{
	@Id
	@Column(length = 32)
	private String id; //记录ID
	@Column(length = 32)
	private String visitorName; //访客名字
	@Column
	private int visitorSex; //访客性别 0为男 1为女
	@Column(length = 32)
	private String visitorPhone; //访客联系电话
	@Column(length = 32)
	private String visitorIdCard; //访客证件号码
	@ManyToOne
	private Room visitRoom; //访问的房间
	@Column
	private long enterTime; //进入公寓楼时间
	@Column
	private long leaveTime; //离开公寓楼时间
	@Column
	private long createTime; //记录创建时间
	
	public VisitRecord()
	{
		
	}
	
	public VisitRecord(String visitorName,int visitorSex,String visitorPhone,String visitorIdCard,Room visitRoom, long enterTime)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())); //对当前nano time进行MD5摘要得出ID
		createTime = System.currentTimeMillis(); //创建时间
		this.visitorName = visitorName;
		this.visitorSex = visitorSex;
		this.visitorPhone = visitorPhone;
		this.visitorIdCard = visitorIdCard;
		this.visitRoom = visitRoom;
		this.enterTime = enterTime;
		this.leaveTime = 0;
	}

	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public int getVisitorSex() {
		return visitorSex;
	}

	public void setVisitorSex(int visitorSex) {
		this.visitorSex = visitorSex;
	}

	public String getVisitorPhone() {
		return visitorPhone;
	}

	public void setVisitorPhone(String visitorPhone) {
		this.visitorPhone = visitorPhone;
	}

	public String getVisitorIdCard() {
		return visitorIdCard;
	}

	public void setVisitorIdCard(String visitorIdCard) {
		this.visitorIdCard = visitorIdCard;
	}

	public Room getVisitRoom() {
		return visitRoom;
	}

	public void setVisitRoom(Room visitRoom) {
		this.visitRoom = visitRoom;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
