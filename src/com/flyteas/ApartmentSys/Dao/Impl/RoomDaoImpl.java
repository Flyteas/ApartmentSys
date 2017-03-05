package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;

/* 房间 数据访问实现  */
@Repository
public class RoomDaoImpl implements RoomDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public Room getById(String id) 
	{
		return ht.get(Room.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getByApartment(Apartment apartment) 
	{
		String hql = "from Room where apartment = ?";
		return (List<Room>)ht.find(hql, apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getEmptyRoom() 
	{
		String hql = "from Room where currentAmout = 0";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getNotFullRoom() 
	{
		String hql = "from Room where currentAmout < capacity";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getFullRoom() 
	{
		String hql = "from Room where currentAmout = capacity";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByRoomName(String name) 
	{
		String hql = "from Room where name like ?";
		return (List<Room>)ht.find(hql,"%"+name+"%");
	}

	@Override
	public boolean add(Room room) 
	{
		try
		{
			ht.save(room);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Room room) 
	{
		try
		{
			ht.update(room);
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
		Room room = getById(id);
		if(room == null)
		{
			return false;
		}
		try
		{
			ht.delete(room);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public int delByApartment(Apartment apartment) 
	{
		List<Room> roomList = getByApartment(apartment);
		if(roomList != null && !roomList.isEmpty())
		{
			ht.deleteAll(roomList);
			return roomList.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getEmptyRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout = 0";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getNotFullRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout < capacity";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getFullRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout = capacity";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByAptRoomName(Apartment apartment, String name) 
	{
		String hql = "from Room where apartment = ? and name like ?";
		return (List<Room>)ht.find(hql,apartment,"%"+name+"%");
	}

}
