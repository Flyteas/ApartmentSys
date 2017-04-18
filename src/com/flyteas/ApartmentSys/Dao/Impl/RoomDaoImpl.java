package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
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
	public List<Room> getByApartment(Apartment apartment,int page,int pageSize) 
	{
		final String hql = "from Room where apartment = ? order by name asc";
		final Apartment apt = apartment;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Room> roomList = ht.execute(new HibernateCallback<List<Room>>()
		{
			public List<Room> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setParameter(0, apt);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				List<Room> roomListRes = query.list();
				return roomListRes;
			}
		});
		return roomList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getEmptyRoom() 
	{
		String hql = "from Room where currentAmout = 0 order by name asc";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getNotFullRoom() 
	{
		String hql = "from Room where currentAmout < capacity order by name asc";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getFullRoom() 
	{
		String hql = "from Room where currentAmout = capacity order by name asc";
		return (List<Room>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByRoomName(String name) 
	{
		String hql = "from Room where name like ? order by name asc";
		return (List<Room>)ht.find(hql,"%"+name+"%");
	}

	@Override
	public boolean add(Room room) 
	{
		try
		{
			room.getApartment().capacityAdd(room.getCapacity());
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
			ht.flush();
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
			room.getApartment().capacitySub(room.getCapacity());
			room.getApartment().usedCapSub(room.getCurrentAmout());
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
		List<Room> roomList = getByApartment(apartment,1,0);
		if(roomList != null && !roomList.isEmpty())
		{
			ht.deleteAll(roomList);
			apartment.setCapacity(0);
			apartment.setUsedCapacity(0);
			return roomList.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getEmptyRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout = 0 order by name asc";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getNotFullRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout < capacity order by name asc";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getFullRoomByApt(Apartment apartment) 
	{
		String hql = "from Room where apartment = ? and currentAmout = capacity order by name asc";
		return (List<Room>)ht.find(hql,apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> findByAptRoom(Apartment apartment, String keyword,int page,int pageSize) 
	{
		final String hql = "from Room where apartment = ? and (id like ? or name like ?) order by name asc";
		final Apartment apt = apartment;
		final String keywordParam = keyword;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Room> roomList = ht.execute(new HibernateCallback<List<Room>>()
		{
			public List<Room> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setParameter(0, apt);
				query.setString(1, "%"+keywordParam+"%");
				query.setString(2, "%"+keywordParam+"%");
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				List<Room> roomListRes = query.list();
				return roomListRes;
			}
		});
		return roomList;
	}

	@Override
	public long getByApartmentSize(Apartment apartment) 
	{
		String hql = "select count(*) from Room where apartment = ?";
		Long result = (Long) ht.find(hql,apartment).listIterator().next();
		return result.longValue();
	}

	@Override
	public long findByAptRoomSize(Apartment apartment, String keyword) 
	{
		String hql = "select count(*) from Room where apartment = ? and (id like ? or name like ?)";
		Long result = (Long) ht.find(hql,apartment,"%"+keyword+"%", "%"+keyword+"%").listIterator().next();
		return result.longValue();
	}

}
