package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 公寓楼业务接口 */
public interface ApartmentSrv 
{
	public List<Apartment> getAllApartment(); //获取所有公寓楼
	public Apartment getApartment(String aptId); //根据ID获取公寓楼
	public List<Apartment> searchApartmentById(String id); //根据公寓楼ID搜索
	public List<Apartment> searchApartmentByName(String name); //根据公寓名字搜索
	public List<Apartment> searchApartmentByAddr(String address); //根据公寓地址搜索
	public boolean addApartment(Apartment apartment); //添加公寓楼
	public boolean modifyApartment(Apartment apartment); //修改公寓楼
	public boolean delApartment(String aptId); //删除公寓楼
	
	public Room getRoom(String roomId); //根据ID获取房间
	public List<Room> getRoomByAptId(String aptId); //根据公寓楼ID获取该楼所有房间 公寓楼不存在返回null
	public List<Room> getRoomNotFullByAptId(String aptId); //根据公寓楼ID获取该楼所有未满房间
	public List<Room> searchRoom(String aptId,String roomName); //根据房间名搜索某公寓楼
	public boolean addRoom(String aptId,Room room); //添加房间
	public boolean modifyRoom(Room room); //修改房间
	public boolean delRoom(String roomId); //删除房间
	
	public List<StuRoom> getAllStuRoom(String roomId); //获取房间所有住宿信息
	public List<StuRoom> getCurrStuRoom(String roomId); //获取房间当前住宿信息
	public int StuIn(String roomId,String stuId,int bedId,long liveStartTime); //学生入住 0成功 -1为Room不存在 -2为Stu不存在 -3为房间满人 -4为床位使用中 -5数据库错误
	public int StuOut(String stuRoomId,long liveEndTime); //学生退住 0成功 -1记录不存在 -2记录已退房 -3数据库错误 
	
	public List<Student> getCurrStuByRoom(String roomId); //获取某房间正在住宿的学生
	public List<Student> getAllStuByRoom(String roomId); //获取某房间所有住宿过的学生
}
