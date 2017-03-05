package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生住宿信息 数据访问实现 */
@Repository
public class StuRoomDaoImpl implements StuRoomDao
{
	@Autowired
	private RoomDao roomDao;

	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public StuRoom getById(String id) 
	{
		return ht.get(StuRoom.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuRoom> getByStudent(Student student) 
	{
		String hql = "from StuRoom where student = ?";
		return (List<StuRoom>) ht.find(hql, student);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuRoom> getAllByRoom(Room room) 
	{
		String hql = "from StuRoom where room = ?";
		return (List<StuRoom>) ht.find(hql, room);
	}

	@Override
	public List<StuRoom> getByApartment(Apartment apartment) 
	{
		List<Room> roomList = roomDao.getByApartment(apartment);
		List<StuRoom> stuRoomList = new ArrayList<StuRoom>();
		if(roomList != null && !roomList.isEmpty())
		{
			for(int i=0;i<roomList.size();i++)
			{
				List<StuRoom> stuRoomListTmp = getAllByRoom(roomList.get(i));
				stuRoomList.addAll(stuRoomListTmp);
			}
		}
		return stuRoomList;
	}

	@Override
	public boolean add(StuRoom stuRoom) 
	{
		try
		{
			ht.save(stuRoom);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(StuRoom stuRoom) 
	{
		try
		{
			ht.update(stuRoom);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean delById(String id) 
	{
		StuRoom stuRoom = getById(id);
		if(stuRoom == null)
		{
			return false;
		}
		try
		{
			ht.delete(stuRoom);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int delByStudent(Student student) 
	{
		List<StuRoom> stuRoomList = (List<StuRoom>) getByStudent(student);
		if(stuRoomList !=null && !stuRoomList.isEmpty())
		{
			ht.deleteAll(stuRoomList);
			return stuRoomList.size();
		}
		return 0;
	}

	@Override
	public int delByRoom(Room room) 
	{
		List<StuRoom> stuRoomList = (List<StuRoom>) getAllByRoom(room);
		if(stuRoomList !=null && !stuRoomList.isEmpty())
		{
			ht.deleteAll(stuRoomList);
			return stuRoomList.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuRoom> getCurrByRoom(Room room) 
	{
		String hql = "from StuRoom where state = 0 and room = ?";
		return (List<StuRoom>) ht.find(hql, room);
	}
}
