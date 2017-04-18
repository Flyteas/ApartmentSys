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

import com.flyteas.ApartmentSys.Dao.VisitRecordDao;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.VisitRecord;

/* 访客记录 数据访问实现 */
@Repository
public class VisitRecordDaoImpl implements VisitRecordDao
{
private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public VisitRecord getById(String id) 
	{
		return ht.get(VisitRecord.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitRecord> getByRoom(Room room) 
	{
		String hql = "from VisitRecord where visitRoom = ?";
		return (List<VisitRecord>)ht.find(hql, room);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitRecord> findById(String id) 
	{
		String hql = "from VisitRecord where id like ?";
		return (List<VisitRecord>)ht.find(hql, "%"+id+"%");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VisitRecord> findByVisitorName(String visitorName) 
	{
		String hql = "from VisitRecord where visitorName like ?";
		return (List<VisitRecord>)ht.find(hql, "%"+visitorName+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VisitRecord> findByIdCard(String visitorIdCard) 
	{
		String hql = "from VisitRecord where visitorIdCard like ?";
		return (List<VisitRecord>)ht.find(hql, "%"+visitorIdCard+"%");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VisitRecord> findByPhone(String phone) 
	{
		String hql = "from VisitRecord where visitorPhone like ?";
		return (List<VisitRecord>)ht.find(hql, "%"+phone+"%");
	}

	@Override
	public boolean add(VisitRecord visitRecord) 
	{
		try
		{
			ht.save(visitRecord);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(VisitRecord visitRecord) 
	{
		try
		{
			ht.clear();
			ht.update(visitRecord);
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
		VisitRecord visitRecord = getById(id);
		if(visitRecord == null)
		{
			return false;
		}
		try
		{
			ht.delete(visitRecord);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public int delByRoom(Room room) 
	{
		List<VisitRecord> visitRecordList = getByRoom(room);
		if(visitRecordList != null && !visitRecordList.isEmpty())
		{
			ht.deleteAll(visitRecordList);
			return visitRecordList.size();
		}
		return 0;
	}

	@Override
	public List<VisitRecord> findByNameOrPhone(String keyword, int page,int pageSize) 
	{
		final String hql = "from VisitRecord where visitorName like ? or visitorPhone like ?";
		final String keywordParam = keyword;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<VisitRecord> recList = ht.execute(new HibernateCallback<List<VisitRecord>>()
		{
			public List<VisitRecord> doInHibernate(Session session) throws HibernateException 
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
				List<VisitRecord> recListRes = query.list();
				return recListRes;
			}
		});
		return recList;
	}

	@Override
	public List<VisitRecord> getAll(int page, int pageSize) 
	{
		final String hql = "from VisitRecord";
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<VisitRecord> recList = ht.execute(new HibernateCallback<List<VisitRecord>>()
		{
			public List<VisitRecord> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<VisitRecord> recListRes = query.list();
				return recListRes;
			}
		});
		return recList;
	}

	@Override
	public long getAllSize() 
	{
		String hql = "select count(*) from VisitRecord";
		Long result = (Long) ht.find(hql).listIterator().next();
		return result.intValue();
	}

	@Override
	public long findByNameOrPhoneSize(String keyword) 
	{
		String hql = "select count(*) from VisitRecord where visitorName like ? or visitorPhone like ?";
		Long result = (Long) ht.find(hql,"%"+keyword+"%", "%"+keyword+"%").listIterator().next();
		return result.intValue();
	}

}
