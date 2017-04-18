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

import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Dao.StudentDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生 数据访问实现 */
@Repository
public class StudentDaoImpl implements StudentDao
{
	@Autowired
	private StuRoomDao stuRoomDao;
	
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public Student getByStuId(String stuId) 
	{
		return ht.get(Student.class, stuId);
	}

	@Override
	public List<Student> getAllByRoom(Room room) 
	{
		List<Student> stuList = new ArrayList<Student>();
		List<StuRoom> stuRoomList = stuRoomDao.getAllByRoom(room);
		if(stuRoomList != null)
		{
			for(int i=0;i<stuRoomList.size();i++)
			{
				stuList.add(stuRoomList.get(i).getStudent());
			}
		}
		return stuList;
	}

	@Override
	public List<Student> getByApartment(Apartment apartment) 
	{
		List<Student> stuList = new ArrayList<Student>();
		List<StuRoom> stuRoomList = stuRoomDao.getByApartment(apartment);
		if(stuRoomList != null)
		{
			for(int i=0;i<stuRoomList.size();i++)
			{
				stuList.add(stuRoomList.get(i).getStudent());
			}
		}
		return stuList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> getNoRoomStu() 
	{
		String hql = "from Student where stuRoom is null";
		return (List<Student>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findByStuId(String stuId) 
	{
		String hql = "from Student where stuId like ?";
		return (List<Student>)ht.find(hql, "%"+stuId+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findByName(String name) 
	{
		String hql = "from Student where name like ?";
		return (List<Student>)ht.find(hql, "%"+name+"%");
	}

	@Override
	public boolean add(Student student) 
	{
		try
		{
			ht.save(student);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Student student) 
	{
		try
		{
			ht.clear();
			ht.update(student);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean delete(String stuId) 
	{
		Student student = getByStuId(stuId);
		if(student == null)
		{
			return false;
		}
		try
		{
			ht.delete(student);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public List<Student> getCurrByRoom(Room room) 
	{
		List<Student> stuList = new ArrayList<Student>();
		List<StuRoom> stuRoomList = stuRoomDao.getCurrByRoom(room);
		if(stuRoomList != null)
		{
			for(int i=0;i<stuRoomList.size();i++)
			{
				StuRoom stuRoom = stuRoomList.get(i);
				if(stuRoom.getState() == 0) //正在住
				{
					stuList.add(stuRoom.getStudent());
				}
			}
		}
		return stuList;
	}

	@Override
	public List<Student> getAll(int page, int pageSize) 
	{
		final String hql = "from Student";
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Student> stuList = ht.execute(new HibernateCallback<List<Student>>()
		{
			public List<Student> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Student> stuListRes = query.list();
				return stuListRes;
			}
		});
		return stuList;
	}

	@Override
	public List<Student> findByIdOrName(String keyword, int page, int pageSize) 
	{
		final String hql = "from Student where stuId like ? or name like ?";
		final String keywordParam = keyword;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Student> stuList = ht.execute(new HibernateCallback<List<Student>>()
		{
			public List<Student> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setString(0, "%"+keywordParam+"%");
				query.setString(1, "%"+keywordParam+"%");
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Student> stuListRes = query.list();
				return stuListRes;
			}
		});
		return stuList;
	}

	@Override
	public long getAllSize() 
	{
		String hql = "select count(*) from Student";
		Long result = (Long) ht.find(hql).listIterator().next();
		return result.intValue();
	}

	@Override
	public long findByIdOrNameSize(String keyword) 
	{
		String hql = "select count(*) from Student where stuId like ? or name like ?";
		Long result = (Long) ht.find(hql,"%"+keyword+"%", "%"+keyword+"%").listIterator().next();
		return result.intValue();
	}
}
