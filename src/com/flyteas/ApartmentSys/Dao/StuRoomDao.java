package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生住宿信息 数据访问接口 */
public interface StuRoomDao 
{
	public StuRoom getById(String id); //通过ID获取
	public List<StuRoom> getByStudent(Student student); //获取某学生住宿信息
	public List<StuRoom> getCurrByRoom(Room room); //获取某房间当前住宿信息
	public List<StuRoom> getAllByRoom(Room room); //获取某房间所有住宿信息
	public List<StuRoom> getByApartment(Apartment apartment); //获取某公寓楼所有住宿信息
	public boolean add(StuRoom stuRoom); //添加
	public boolean saveModify(StuRoom stuRoom); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByStudent(Student student); //删除某学生住宿信息
	public int delByRoom(Room room); //删除某房间所有住宿信息
}
