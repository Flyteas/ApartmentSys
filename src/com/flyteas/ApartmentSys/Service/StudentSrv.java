package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生业务接口 */
public interface StudentSrv 
{
	public Student getById(String stuId); //根据学号获取
	public List<Student> searchById(String stuId); //根据学号搜索
	public List<Student> searchByName(String name); //根据姓名搜索
	public int addStu(String stuId,String name,int sex,String phone); //添加学生 0成功 -1学号存在 -2数据库错误
	public int modifyStu(String stuId,String name,int sex,String phone); //修改学生 0成功 -1学号不存在 -2数据库错误
	public int delStu(String stuId); //删除学生 0成功 -1学号不存在 -2数据库错误
	public List<StuRoom> getStuRoom(String stuId); //获取学生住宿信息 学生不存在返回null
}
