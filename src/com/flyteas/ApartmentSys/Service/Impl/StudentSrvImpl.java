package com.flyteas.ApartmentSys.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Dao.StudentDao;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Service.StudentSrv;

/* 学生业务实现 */
@Service
public class StudentSrvImpl implements StudentSrv
{
	@Autowired
	private StudentDao stuDao;
	@Autowired
	private StuRoomDao stuRoomDao;
	
	@Override
	public Student getById(String stuId) 
	{
		return stuDao.getByStuId(stuId);
	}

	@Override
	public List<Student> searchById(String stuId) 
	{
		return stuDao.findByStuId(stuId);
	}

	@Override
	public List<Student> searchByName(String name) 
	{
		return stuDao.findByName(name);
	}

	@Transactional
	@Override
	public int addStu(String stuId, String name, int sex, String phone) 
	{
		if(stuDao.getByStuId(stuId) != null) //学号已存在
		{
			return -1;
		}
		Student stu = new Student(stuId,name,sex,phone);
		if(!stuDao.add(stu)) //添加失败
		{
			return -2;
		}
		return 0;
	}

	@Transactional
	@Override
	public int modifyStu(String stuId, String name, int sex, String phone) 
	{
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null) //学号不存在
		{
			return -1;
		}
		stu.setName(name);
		stu.setSex(sex);
		stu.setPhone(phone);
		if(!stuDao.saveModify(stu)) //保存失败
		{
			return -2;
		}
		return 0;
	}

	@Transactional
	@Override
	public int delStu(String stuId) 
	{
		if(!stuDao.delete(stuId)) //删除失败
		{
			return -1;
		}
		return 0;
	}

	@Override
	public List<StuRoom> getStuRoom(String stuId) 
	{
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null)
		{
			return null;
		}
		return stuRoomDao.getByStudent(stu);
	}

}
