package com.flyteas.ApartmentSys.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 房间  实体类 */
@Entity
@Table
public class Room 
{
	@Id
	@Column(length = 32)
	private String id; //房间ID
	@Column(length = 16)
	private String name; //房间名
	@ManyToOne
	private Apartment apartment; //所属公寓楼
	@Column
	private int capacity; //房间人数最大容量
	@Column
	private int currentAmout; //当前房间人数，添加并维护这个属性的目的在于提高查找空房间时的性能
	@Column
	private long createTime; //房间添加时间 时间戳格式
	@Column
	private float price; //住宿价格
	
	public Room() //空构造方法，必须有，否则无法注入
	{
		
	}
	
	public Room(String roomName,Apartment roomApartment,int roomCapacity,float roomPrice)
	{
		id = MD5Encryptor.md5Encrypt(String.valueOf(System.nanoTime())).substring(8, 24); //对当前nano time进行MD5摘要并取中间16位得出ID
		createTime = System.currentTimeMillis(); //创建时间
		name = roomName; //房间名称
		apartment = roomApartment; //所属公寓楼
		capacity = roomCapacity; //容纳人数
		currentAmout = 0;
		price = roomPrice;
	}
	
	public boolean isFull() //房间是否还有空位
	{
		return this.currentAmout >= this.capacity;
	}
	
	public void currAmoutInc() //当前住宿人数增加1
	{
		this.currentAmout++;
	}
	
	public void currAmoutDec() //当前住宿人数增加1
	{
		this.currentAmout--;
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

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCurrentAmout() {
		return currentAmout;
	}

	public void setCurrentAmout(int currentAmout) {
		this.currentAmout = currentAmout;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
}
