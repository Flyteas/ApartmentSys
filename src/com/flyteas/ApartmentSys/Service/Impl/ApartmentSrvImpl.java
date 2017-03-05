package com.flyteas.ApartmentSys.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.ApartmentDao;
import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Dao.StudentDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;

/* 公寓楼业务实现 */
@Transactional
@Service
public class ApartmentSrvImpl implements ApartmentSrv
{
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private StuRoomDao stuRoomDao;
	@Autowired
	private StudentDao stuDao;
	
	@Override
	public List<Apartment> getAllApartment() 
	{
		return apartmentDao.getAll();
	}

	@Override
	public Apartment getApartment(String aptId) 
	{
		return apartmentDao.getById(aptId);
	}

	@Override
	public List<Apartment> searchApartmentById(String id) 
	{
		return apartmentDao.findById(id);
	}

	@Override
	public List<Apartment> searchApartmentByName(String name) 
	{
		return apartmentDao.findByName(name);
	}

	@Override
	public List<Apartment> searchApartmentByAddr(String address) 
	{
		return apartmentDao.findByAddr(address);
	}

	@Override
	public boolean addApartment(Apartment apartment) 
	{
		return apartmentDao.add(apartment);
	}

	@Override
	public boolean modifyApartment(Apartment apartment) 
	{
		return apartmentDao.saveModify(apartment);
	}

	@Override
	public boolean delApartment(String aptId) 
	{
		return apartmentDao.delete(aptId);
	}

	@Override
	public Room getRoom(String roomId) 
	{
		return roomDao.getById(roomId);
	}

	@Override
	public List<Room> getRoomByAptId(String aptId) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		return roomDao.getByApartment(apt);
	}

	@Override
	public List<Room> getRoomNotFullByAptId(String aptId) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		return roomDao.getNotFullRoomByApt(apt);
	}

	@Override
	public List<Room> searchRoom(String aptId, String roomName) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		return roomDao.findByAptRoomName(apt, roomName);
	}

	@Override
	public boolean addRoom(String aptId, Room room) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return false;
		}
		room.setApartment(apt);
		return roomDao.add(room);
	}

	@Override
	public boolean modifyRoom(Room room) 
	{
		return roomDao.saveModify(room);
	}

	@Override
	public boolean delRoom(String roomId) 
	{
		return roomDao.delById(roomId);
	}

	@Override
	public List<StuRoom> getAllStuRoom(String roomId) 
	{
		Room room = getRoom(roomId);
		if(room == null)
		{
			return null;
		}
		return stuRoomDao.getAllByRoom(room);
	}

	@Override
	public List<StuRoom> getCurrStuRoom(String roomId) 
	{
		Room room = getRoom(roomId);
		if(room == null)
		{
			return null;
		}
		return stuRoomDao.getCurrByRoom(room);
	}
	
	@Override
	public int StuIn(String roomId, String stuId, int bedId, long liveStartTime) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return -1;
		}
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null) //学生不存在
		{
			return -2;
		}
		if(room.isFull()) //房间满人
		{
			return -3;
		}
		List<StuRoom> stuRoomList = stuRoomDao.getCurrByRoom(room); //获取房间当前住宿信息
		for(int i=0;i<stuRoomList.size();i++)
		{
			if(bedId == stuRoomList.get(i).getBedId()) //床位使用中
			{
				return -4;
			}
		}
		StuRoom stuRoom = new StuRoom(room,bedId,stu,liveStartTime);
		if(!stuRoomDao.add(stuRoom)) //添加失败
		{
			return -5;
		}
		room.currAmoutInc(); //房间人数加1
		return 0;
	}

	@Override
	public int StuOut(String stuRoomId, long liveEndTime) 
	{
		StuRoom stuRoom = stuRoomDao.getById(stuRoomId);
		if(stuRoom == null) //住宿记录不存在
		{
			return -1;
		}
		if(stuRoom.getState() == 1) //该记录已经退房
		{
			return -2;
		}
		Room room = stuRoom.getRoom();
		if(!stuRoomDao.delById(stuRoomId)) //删除出错
		{
			return -3;
		}
		room.currAmoutDec(); //当前房间住宿人数减1
		return 0;
	}

	@Override
	public List<Student> getCurrStuByRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return null;
		}
		return stuDao.getCurrByRoom(room);
	}

	@Override
	public List<Student> getAllStuByRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return null;
		}
		return stuDao.getAllByRoom(room);
	}


}
