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
	public List<Student> searchByIdOrName(String keyword,int page,int pageSize); //根据学号或者姓名搜索 分页 page为1 pageSize为0则返回所有记录
	public long searchByIdOrNameSize(String keyword); //根据学号或者姓名搜索结果数
	public int addStu(String stuId,String name,int sex,String phone); //添加学生 0成功 -1学号存在 -2数据库错误
	public int modifyStu(String stuId,String name,int sex,String phone); //修改学生 0成功 -1学号不存在 -2数据库错误
	public int delStu(String stuId); //删除学生 0成功 -1学号不存在 -2数据库错误
	public boolean delStuRoom(String id); //删除住宿信息
	public List<StuRoom> getStuRoom(String stuId,int state,int page,int pageSize); //获取学生住宿信息 学生不存在返回null 分页  state为0返回正在住宿信息 1返回已退住宿 2返回所有
	public long getStuRoomSize(String stuId,int state); //获取学生住宿信息记录数  state为0返回正在住宿信息 1返回已退住宿 2返回所有
	
	public int addStuAccess(String stuId,String aptId,int type); //添加门禁记录 type为0表示进入记录 1出记录 返回 0成功 -1学生不存在 -2公寓不存在 -3数据库错误
}
