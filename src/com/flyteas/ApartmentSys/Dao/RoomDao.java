package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;

/* 房间 数据访问接口 */
public interface RoomDao 
{
	public Room getById(String id); //通过ID获取
	public List<Room> getByApartment(Apartment apartment,int page,int pageSize); //获取公寓楼的所有房间 分页
	public long getByApartmentSize(Apartment apartment); //获取公寓楼的所有房间数量
	public List<Room> getEmptyRoomByApt(Apartment apartment); //获取某公寓楼所有空房间
	public List<Room> getNotFullRoomByApt(Apartment apartment); //获取某公寓楼所有未满房间
	public List<Room> getFullRoomByApt(Apartment apartment); //获取某公寓楼所以已满房间
	public List<Room> getEmptyRoom(); //获取所有空房间
	public List<Room> getNotFullRoom(); //获取所有未满房间
	public List<Room> getFullRoom(); //获取所以已满房间
	public List<Room> findByAptRoom(Apartment apartment,String keyword,int page,int pageSize); //通过ID或房间名称查找某公寓楼 模糊查找 分页
	public long findByAptRoomSize(Apartment apartment,String keyword); //通过ID或房间名称查找某公寓楼 模糊查找 结果数
	public List<Room> findByRoomName(String name); //通过房间名称查找 模糊查找
	public boolean add(Room room); //添加
	public boolean saveModify(Room room); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByApartment(Apartment apartment); //删除公寓楼的所有房间
}
