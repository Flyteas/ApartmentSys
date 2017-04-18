package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.ArrayList;
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
	
	@Override
	public List<StuRoom> getByStudent(Student student, int page,int pageSize)
	{
		final String hql = "from StuRoom where student = ?";
		final Student stuParam = student;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<StuRoom> stuRoomList = ht.execute(new HibernateCallback<List<StuRoom>>()
		{
			public List<StuRoom> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setParameter(0, stuParam);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<StuRoom> stuRoomListRes = query.list();
				return stuRoomListRes;
			}
		});
		return stuRoomList;
	}

	@Override
	public List<StuRoom> getByStudentState(Student student,int state, int page,int pageSize) 
	{
		final String hql = "from StuRoom where student = ? and state = ?";
		final Student stuParam = student;
		final int stateParam = state;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<StuRoom> stuRoomList = ht.execute(new HibernateCallback<List<StuRoom>>()
		{
			public List<StuRoom> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setParameter(0, stuParam);
				query.setInteger(1, stateParam);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<StuRoom> stuRoomListRes = query.list();
				return stuRoomListRes;
			}
		});
		return stuRoomList;
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
		List<Room> roomList = roomDao.getByApartment(apartment,1,0);
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
			ht.flush();
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
			ht.clear();
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

	@Override
	public int delByStudent(Student student) 
	{
		List<StuRoom> stuRoomList = getByStudent(student,1,0);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuRoom> getExitByRoom(Room room)
	{
		String hql = "from StuRoom where state = 1 and room = ?";
		return (List<StuRoom>) ht.find(hql, room);
	}

	@Override
	public long getByStudentSize(Student student) 
	{
		String hql = "select count(*) from StuRoom where student = ?";
		Long result = (Long) ht.find(hql,student).listIterator().next();
		return result.intValue();
	}

	@Override
	public long getByStudentStateSize(Student student, int state) 
	{
		String hql = "select count(*) from StuRoom where student = ? and state = ?";
		Long result = (Long) ht.find(hql,student,state).listIterator().next();
		return result.intValue();
	}
}
