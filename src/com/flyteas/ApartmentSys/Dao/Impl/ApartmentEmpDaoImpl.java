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

import com.flyteas.ApartmentSys.Dao.ApartmentEmpDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;

/* 公寓职员 数据访问实现类 */
@Repository
public class ApartmentEmpDaoImpl implements ApartmentEmpDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public ApartmentEmp getById(String id) 
	{
		return ht.get(ApartmentEmp.class, id);
	}

	public ApartmentEmp getByAptEmp(Apartment apt,Employee emp)
	{
		String hql = "from ApartmentEmp where apartment = ? and employee = ?";
		@SuppressWarnings("unchecked")
		List<ApartmentEmp> aptEmpList =  (List<ApartmentEmp>)ht.find(hql, apt,emp);
		if(aptEmpList.isEmpty()) //如果结果为空
		{
			return null;
		}
		return aptEmpList.get(0); //返回第一条记录
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ApartmentEmp> getByApartment(Apartment apartment) 
	{
		String hql = "from ApartmentEmp where apartment = ?";
		return (List<ApartmentEmp>)ht.find(hql, apartment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApartmentEmp> getByEmployee(Employee employee) 
	{
		String hql = "from ApartmentEmp where employee = ?";
		return (List<ApartmentEmp>)ht.find(hql, employee);
	}

	@Override
	public boolean add(ApartmentEmp apartmentEmp) 
	{
		try
		{
			ht.save(apartmentEmp);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(ApartmentEmp apartmentEmp) 
	{
		try
		{
			ht.clear();
			ht.update(apartmentEmp);
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
		ApartmentEmp apartmentEmp = getById(id);
		if(apartmentEmp == null)
		{
			return false;
		}
		try
		{
			ht.delete(apartmentEmp);
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
		List<ApartmentEmp> apartmentEmpList = getByApartment(apartment);
		if(apartmentEmpList != null && apartmentEmpList.size() > 0)
		{
			ht.deleteAll(apartmentEmpList);
		}
		return apartmentEmpList.size();
	}

	@Override
	public int delByEmployee(Employee employee) 
	{
		List<ApartmentEmp> apartmentEmpList = getByEmployee(employee);
		if(apartmentEmpList != null && apartmentEmpList.size() > 0)
		{
			ht.deleteAll(apartmentEmpList);
		}
		return apartmentEmpList.size();
	}

	@Override
	public List<ApartmentEmp> getByAptState(Apartment apartment, int empState,int page, int pageSize) 
	{
		final Apartment aptParam = apartment;
		final int empStateParam = empState;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<ApartmentEmp> aptEmpList = ht.execute(new HibernateCallback<List<ApartmentEmp>>()
		{
			public List<ApartmentEmp> doInHibernate(Session session) throws HibernateException 
			{
				Query query;
				if(empStateParam == 2) //获取全部
				{
					String hql = "from ApartmentEmp where apartment = ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
				}
				else
				{
					String hql = "from ApartmentEmp where apartment = ? and employee.state = ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
					query.setInteger(1, empStateParam);
				}
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<ApartmentEmp> aptEmpListRes = query.list();
				return aptEmpListRes;
			}
		});
		return aptEmpList;
	}

	@Override
	public long getByAptStateSize(Apartment apartment, int empState) 
	{
		String hql;
		Long result;
		if(empState == 2) //全部
		{
			hql = "select count(*) from ApartmentEmp where apartment = ?";
			result = (Long) ht.find(hql,apartment).listIterator().next();
		}
		else
		{
			hql = "select count(*) from ApartmentEmp where apartment = ? and employee.state = ?";
			result = (Long) ht.find(hql,apartment,empState).listIterator().next();
		}
		return result.longValue();
	}

	@Override
	public List<ApartmentEmp> getByEmp(Employee emp, int page, int pageSize) 
	{
		final String hql = "from ApartmentEmp where employee = ?";
		final Employee empParam = emp;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<ApartmentEmp> aptEmpList = ht.execute(new HibernateCallback<List<ApartmentEmp>>()
		{
			public List<ApartmentEmp> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setParameter(0, empParam);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<ApartmentEmp> aptEmpListRes = query.list();
				return aptEmpListRes;
			}
		});
		return aptEmpList;
	}

	@Override
	public long getByEmpSize(Employee emp) 
	{
		String hql = "select count(*) from ApartmentEmp where employee = ?";
		return (Long) ht.find(hql,emp).listIterator().next();
	}
}
