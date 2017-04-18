package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生 数据访问接口 */
public interface StudentDao 
{
	public Student getByStuId(String stuId); //通过学号获取
	public List<Student> getCurrByRoom(Room room); //获取某个房间当前住着的学生
	public List<Student> getAllByRoom(Room room); //获取某个房间的所有住过的学生
	public List<Student> getByApartment(Apartment apartment); //获取某公寓楼所有学生
	public List<Student> getNoRoomStu(); //获取所有未有住宿的学生
	public List<Student> getAll(int page,int pageSize); //获取所有，分页
	public List<Student> findByStuId(String stuId); //通过学号查找 模糊查找
	public List<Student> findByName(String name); //通过名字查找 模糊查找
	public List<Student> findByIdOrName(String keyword,int page,int pageSize); //通过学号或者姓名 模糊查找 分页
	public long getAllSize(); //获取所有学生数
	public long findByIdOrNameSize(String keyword); //获取搜索结果数
	public boolean add(Student student); //添加
	public boolean saveModify(Student student); //保存修改
	public boolean delete(String stuId); //通过学号删除
}
