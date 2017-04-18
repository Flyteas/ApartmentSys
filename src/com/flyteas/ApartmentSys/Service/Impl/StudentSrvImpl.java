package com.flyteas.ApartmentSys.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.ApartmentDao;
import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Dao.StuAccessRecordDao;
import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Dao.StudentDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuAccessRecord;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Service.StudentSrv;

/* 学生业务实现 */
@Service
@Transactional
public class StudentSrvImpl implements StudentSrv
{
	@Autowired
	private StudentDao stuDao;
	@Autowired
	private StuRoomDao stuRoomDao;
	@Autowired
	private ApartmentDao aptDao;
	@Autowired
	private StuAccessRecordDao stuAccDao;
	@Autowired
	private RoomDao roomDao;
	
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
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null) //学生不存在
		{
			return -1;
		}
		List<StuRoom> stuRoomList = stuRoomDao.getByStudent(stu, 1, 0);
		for(int i=0;i<stuRoomList.size();i++) //删除全部
		{
			StuRoom stuRoom = stuRoomList.get(i);
			this.delStuRoom(stuRoom.getId());
		}
		if(!stuDao.delete(stuId)) //删除失败
		{
			return -1;
		}
		return 0;
	}

	@Override
	public boolean delStuRoom(String id) 
	{
		StuRoom stuRoom = stuRoomDao.getById(id);
		if(stuRoom == null)
		{
			return false;
		}
		if(stuRoom.getState() == 0) //状态为正在住宿
		{
			Room room = stuRoom.getRoom();
			room.currAmoutDec(); //房间人数-1
			Apartment apt = room.getApartment();
			apt.usedCapSub(1); //公寓人数-1
			aptDao.saveModify(apt);
			roomDao.saveModify(room);
		}
		return stuRoomDao.delById(id);
	}
	
	@Override
	public List<StuRoom> getStuRoom(String stuId,int state,int page,int pageSize) 
	{
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null)
		{
			return null;
		}
		if(state == 2)
		{
			return stuRoomDao.getByStudent(stu,page,pageSize);
		}
		return stuRoomDao.getByStudentState(stu, state, page, pageSize);
	}

	@Override
	public List<Student> searchByIdOrName(String keyword,int page,int pageSize) 
	{
		if(keyword.isEmpty())
		{
			return stuDao.getAll(page, pageSize); //返回所有结果
		}
		return stuDao.findByIdOrName(keyword, page, pageSize);
	}

	@Override
	public long searchByIdOrNameSize(String keyword) 
	{
		if(keyword.isEmpty())
		{
			return stuDao.getAllSize();
		}
		return stuDao.findByIdOrNameSize(keyword);
	}

	@Override
	public long getStuRoomSize(String stuId, int state) 
	{
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null)
		{
			return 0;
		}
		if(state == 2)
		{
			return stuRoomDao.getByStudentSize(stu);
		}
		return stuRoomDao.getByStudentStateSize(stu, state);
	}

	@Override
	public int addStuAccess(String stuId, String aptId, int type) 
	{
		if(stuId == null || stuId.isEmpty())
		{
			return -1;
		}
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null)
		{
			return -1;
		}
		if(aptId == null || aptId.isEmpty())
		{
			return -2;
		}
		Apartment apt = aptDao.getById(aptId);
		if(apt == null)
		{
			return -2;
		}
		StuAccessRecord stuAcc = new StuAccessRecord(stu,apt,type);
		if(!stuAccDao.add(stuAcc)) //添加失败
		{
			return -3;
		}
		return 0;
	}

}
