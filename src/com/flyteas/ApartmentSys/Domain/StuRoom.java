package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 学生住房信息 实体类 */
@Entity
@Table
public class StuRoom 
{
	@Id
	@Column(length = 32)
	private String id; //住房信息ID
	@ManyToOne
	private Room room; //所在房间
	@ManyToOne
	private Student student; //住房信息对应的学生
	@Column
	private int state; //住宿状态，0为正在住宿，1为已退住
	@Column
	private long liveStartTime; //住宿开始时间
	@Column
	private long liveEndTime; //住宿结束时间
	@Column
	private long createTime; //添加时间，时间戳格式
	
	public StuRoom() //空构造方法，必须有，否则无法注入
	{
		
	}
	
	public StuRoom(Room room,Student student,int state,long liveStartTime,long liveEndTime)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(8, 24); //对当前nano time进行MD5摘要并取中间16位得出ID
		createTime = System.currentTimeMillis(); //创建时间
		this.room = room;
		this.student = student;
		this.state = state;
		this.liveStartTime = liveStartTime;
		this.liveEndTime = liveEndTime;
	}

	/* setter和getter */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getLiveStartTime() {
		return liveStartTime;
	}

	public void setLiveStartTime(long liveStartTime) {
		this.liveStartTime = liveStartTime;
	}

	public long getLiveEndTime() {
		return liveEndTime;
	}

	public void setLiveEndTime(long liveEndTime) {
		this.liveEndTime = liveEndTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	
}
