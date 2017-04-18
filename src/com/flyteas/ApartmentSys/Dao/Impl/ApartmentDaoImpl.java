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

import com.flyteas.ApartmentSys.Dao.ApartmentDao;
import com.flyteas.ApartmentSys.Domain.Apartment;

/* 公寓楼 数据访问接口实现 */
@Repository
public class ApartmentDaoImpl implements ApartmentDao
{
	private HibernateTemplate ht;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Apartment getById(String id) 
	{
		return ht.get(Apartment.class,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> getAll() 
	{
		String hql = "from Apartment order by name asc";
		return (List<Apartment>)ht.find(hql);
	}
	
	@Override
	public List<Apartment> getAll(int page,int pageSize) //获取所有 分页 
	{
		final String hql = "from Apartment order by name asc";
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Apartment> aptList = ht.execute(new HibernateCallback<List<Apartment>>()
		{
			public List<Apartment> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Apartment> aptListRes = query.list();
				return aptListRes;
			}
		});
		return aptList;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> findByName(String name) 
	{
		String hql = "from Apartment where name like ? order by name asc";
		return (List<Apartment>)ht.find(hql,"%"+name+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> findByAddr(String address) 
	{
		String hql = "from Apartment where address like ? order by name asc";
		return (List<Apartment>)ht.find(hql,"%"+address+"%");
	}

	@Override
	public List<Apartment> findByNameOrAddr(String keyword,int page,int pageSize)
	{
		final String hql = "from Apartment where name like ? or address like ? order by name asc";
		final String keywordParam = keyword;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Apartment> aptList = ht.execute(new HibernateCallback<List<Apartment>>()
		{
			public List<Apartment> doInHibernate(Session session) throws HibernateException 
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
				List<Apartment> aptListRes = query.list();
				return aptListRes;
			}
		});
		return aptList;
	}
	
	@Override
	public boolean add(Apartment apartment) 
	{
		try
		{
			ht.save(apartment);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Apartment apartment) 
	{
		try
		{
			ht.update(apartment);
			ht.flush();
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean delete(String id) 
	{
		Apartment apartment = getById(id);
		if(apartment == null) //ID对应的apartment不存在
		{
			return false;
		}
		try
		{
			ht.delete(apartment);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> findById(String id) 
	{
		String hql = "from Apartment where id like ? order by name asc";
		return (List<Apartment>)ht.find(hql, "%"+id+"%");
	}
	
	@Override
	public long getAllSize() //获取所有记录数
	{
		String hql = "select count(*) from Apartment";
		Long result = (Long) ht.find(hql).listIterator().next();
		return result.intValue();
	}
	
	@Override
	public long findByNameOrAddrSize(String keyword)
	{
		String hql = "select count(*) from Apartment where name like ? or address like ? order by name asc";
		Long result = (Long) ht.find(hql,"%"+keyword+"%", "%"+keyword+"%").listIterator().next();
		return result.intValue();
	}
}
