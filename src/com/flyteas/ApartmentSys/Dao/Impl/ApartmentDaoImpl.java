package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		String hql = "from Apartment";
		return (List<Apartment>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> findByName(String name) 
	{
		String hql = "from Apartment where name like ?";
		return (List<Apartment>)ht.find(hql,"%"+name+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Apartment> findByAddr(String address) 
	{
		String hql = "from Apartment where address like ?";
		return (List<Apartment>)ht.find(hql,"%"+address+"%");
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
		String hql = "from Apartment where id like ?";
		return (List<Apartment>)ht.find(hql, "%"+id+"%");
	}
}
