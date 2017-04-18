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

import com.flyteas.ApartmentSys.Dao.ApartmentEmpDao;
import com.flyteas.ApartmentSys.Dao.RotaDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Rota;

/* 值班信息 数据访问实现 */
@Repository
public class RotaDaoImpl implements RotaDao
{
	@Autowired
	private ApartmentEmpDao apartmentEmpDao;
	
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public Rota getById(String id) 
	{
		return ht.get(Rota.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> getByApartmentEmp(ApartmentEmp apartmentEmp) 
	{
		String hql = "from Rota where apartmentEmp = ?";
		return (List<Rota>)ht.find(hql, apartmentEmp);
		
	}

	@Override
	public List<Rota> getByApartment(Apartment apartment) 
	{
		List<Rota> rotaList = new ArrayList<Rota>();
		List<ApartmentEmp> apartmentEmpList = apartmentEmpDao.getByApartment(apartment);
		for(int i=0;i<apartmentEmpList.size();i++)
		{
			List<Rota> rotaListTmp = getByApartmentEmp(apartmentEmpList.get(i));
			rotaList.addAll(rotaListTmp);
		}
		return rotaList;
	}

	@Override
	public List<Rota> getByEmployee(Employee employee) 
	{
		List<Rota> rotaList = new ArrayList<Rota>();
		List<ApartmentEmp> apartmentEmpList = apartmentEmpDao.getByEmployee(employee);
		for(int i=0;i<apartmentEmpList.size();i++)
		{
			List<Rota> rotaListTmp = getByApartmentEmp(apartmentEmpList.get(i));
			rotaList.addAll(rotaListTmp);
		}
		return rotaList;
	}

	@Override
	public boolean add(Rota rota) 
	{
		try
		{
			ht.save(rota);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Rota rota) 
	{
		try
		{
			ht.clear();
			ht.update(rota);
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
		Rota rota = getById(id);
		if(rota == null)
		{
			return false;
		}
		try
		{
			ht.delete(rota);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public int delByApartmentEmp(ApartmentEmp apartmentEmp) 
	{
		List<Rota> rotaList = getByApartmentEmp(apartmentEmp);
		if(rotaList != null && !rotaList.isEmpty())
		{
			ht.deleteAll(rotaList);
			return rotaList.size();
		}
		return 0;
	}

	@Override
	public int delByApartment(Apartment apartment) 
	{
		List<Rota> rotaList = getByApartment(apartment);
		if(rotaList != null && !rotaList.isEmpty())
		{
			ht.deleteAll(rotaList);
			return rotaList.size();
		}
		return 0;
	}

	@Override
	public int delByEmployee(Employee employee) {
		List<Rota> rotaList = getByEmployee(employee);
		if(rotaList != null && !rotaList.isEmpty())
		{
			ht.deleteAll(rotaList);
			return rotaList.size();
		}
		return 0;
	}

	@Override
	public List<Rota> findByAptTime(Apartment apartment, long startTime,long endTime, int page, int pageSize) 
	{
		final Apartment aptParam = apartment;
		final long startTimeParam = startTime;
		final long endTimeParam = endTime;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Rota> rotaList = ht.execute(new HibernateCallback<List<Rota>>()
		{
			public List<Rota> doInHibernate(Session session) throws HibernateException 
			{
				String hql;
				Query query;
				if(startTimeParam == -1 && endTimeParam == -1) //开始时间结束时间均任意
				{
					hql= "from Rota where apartmentEmp.apartment = ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
				}
				else if(startTimeParam == -1 && endTimeParam != -1) //开始时间任意
				{
					hql= "from Rota where apartmentEmp.apartment = ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
					query.setLong(1, endTimeParam);
				}
				else if(startTimeParam != -1 && endTimeParam == -1) //结束时间任意
				{
					hql= "from Rota where apartmentEmp.apartment = ? and startTime >= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
					query.setLong(1, startTimeParam);
				}
				else
				{
					hql= "from Rota where apartmentEmp.apartment = ? and startTime >= ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptParam);
					query.setLong(1, startTimeParam);
					query.setLong(2, endTimeParam);
				}
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Rota> rotaListRes = query.list();
				return rotaListRes;
			}
		});
		return rotaList;
	}
	
	@Override
	public long findByAptTimeSize(Apartment apartment,long startTime,long endTime)
	{
		String hql;
		Long result;
		if(startTime == -1 && endTime == -1) //开始时间结束时间均任意
		{
			hql = "select count(*) from Rota where apartmentEmp.apartment = ?";
			result = (Long)ht.find(hql,apartment).listIterator().next();
		}
		else if(startTime == -1 && endTime != -1) //开始时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp.apartment = ? and endTime <= ?";
			result = (Long)ht.find(hql,apartment,endTime).listIterator().next();
		}
		else if(startTime != -1 && endTime == -1) //结束时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp.apartment = ? and startTime >= ?";
			result = (Long)ht.find(hql,apartment,startTime).listIterator().next();
		}
		else
		{
			hql= "select count(*) from Rota where apartmentEmp.apartment = ? and startTime >= ? and endTime <= ?";
			result = (Long)ht.find(hql,apartment,startTime,endTime).listIterator().next();
		}
		return result.longValue();
	}

	@Override
	public List<Rota> findByAptEmpTime(ApartmentEmp aptEmp, long startTime,long endTime, int page, int pageSize) 
	{
		final ApartmentEmp aptEmpParam = aptEmp;
		final long startTimeParam = startTime;
		final long endTimeParam = endTime;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Rota> rotaList = ht.execute(new HibernateCallback<List<Rota>>()
		{
			public List<Rota> doInHibernate(Session session) throws HibernateException 
			{
				String hql;
				Query query;
				if(startTimeParam == -1 && endTimeParam == -1) //开始时间结束时间均任意
				{
					hql= "from Rota where apartmentEmp = ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptEmpParam);
				}
				else if(startTimeParam == -1 && endTimeParam != -1) //开始时间任意
				{
					hql= "from Rota where apartmentEmp = ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptEmpParam);
					query.setLong(1, endTimeParam);
				}
				else if(startTimeParam != -1 && endTimeParam == -1) //结束时间任意
				{
					hql= "from Rota where apartmentEmp = ? and startTime >= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptEmpParam);
					query.setLong(1, startTimeParam);
				}
				else
				{
					hql= "from Rota where apartmentEmp = ? and startTime >= ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, aptEmpParam);
					query.setLong(1, startTimeParam);
					query.setLong(2, endTimeParam);
				}
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Rota> rotaListRes = query.list();
				return rotaListRes;
			}
		});
		return rotaList;
	}

	@Override
	public long findByAptEmpTimeSize(ApartmentEmp aptEmp, long startTime,long endTime) 
	{
		String hql;
		Long result;
		if(startTime == -1 && endTime == -1) //开始时间结束时间均任意
		{
			hql = "select count(*) from Rota where apartmentEmp = ?";
			result = (Long)ht.find(hql,aptEmp).listIterator().next();
		}
		else if(startTime == -1 && endTime != -1) //开始时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp = ? and endTime <= ?";
			result = (Long)ht.find(hql,aptEmp,endTime).listIterator().next();
		}
		else if(startTime != -1 && endTime == -1) //结束时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp = ? and startTime >= ?";
			result = (Long)ht.find(hql,aptEmp,startTime).listIterator().next();
		}
		else
		{
			hql= "select count(*) from Rota where apartmentEmp = ? and startTime >= ? and endTime <= ?";
			result = (Long)ht.find(hql,aptEmp,startTime,endTime).listIterator().next();
		}
		return result.longValue();
	}

	@Override
	public List<Rota> findByEmpTime(Employee emp, long startTime, long endTime,int page, int pageSize) 
	{
		final Employee empParam = emp;
		final long startTimeParam = startTime;
		final long endTimeParam = endTime;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Rota> rotaList = ht.execute(new HibernateCallback<List<Rota>>()
		{
			public List<Rota> doInHibernate(Session session) throws HibernateException 
			{
				String hql;
				Query query;
				if(startTimeParam == -1 && endTimeParam == -1) //开始时间结束时间均任意
				{
					hql= "from Rota where apartmentEmp.employee = ?";
					query = session.createQuery(hql);
					query.setParameter(0, empParam);
				}
				else if(startTimeParam == -1 && endTimeParam != -1) //开始时间任意
				{
					hql= "from Rota where apartmentEmp.employee = ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, empParam);
					query.setLong(1, endTimeParam);
				}
				else if(startTimeParam != -1 && endTimeParam == -1) //结束时间任意
				{
					hql= "from Rota where apartmentEmp.employee = ? and startTime >= ?";
					query = session.createQuery(hql);
					query.setParameter(0, empParam);
					query.setLong(1, startTimeParam);
				}
				else
				{
					hql= "from Rota where apartmentEmp.employee = ? and startTime >= ? and endTime <= ?";
					query = session.createQuery(hql);
					query.setParameter(0, empParam);
					query.setLong(1, startTimeParam);
					query.setLong(2, endTimeParam);
				}
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Rota> rotaListRes = query.list();
				return rotaListRes;
			}
		});
		return rotaList;
	}

	@Override
	public long findByEmpTimeSize(Employee emp, long startTime, long endTime) 
	{
		String hql;
		Long result;
		if(startTime == -1 && endTime == -1) //开始时间结束时间均任意
		{
			hql = "select count(*) from Rota where apartmentEmp.employee = ?";
			result = (Long)ht.find(hql,emp).listIterator().next();
		}
		else if(startTime == -1 && endTime != -1) //开始时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp.employee = ? and endTime <= ?";
			result = (Long)ht.find(hql,emp,endTime).listIterator().next();
		}
		else if(startTime != -1 && endTime == -1) //结束时间任意
		{
			hql= "select count(*) from Rota where apartmentEmp.employee = ? and startTime >= ?";
			result = (Long)ht.find(hql,emp,startTime).listIterator().next();
		}
		else
		{
			hql= "select count(*) from Rota where apartmentEmp.employee = ? and startTime >= ? and endTime <= ?";
			result = (Long)ht.find(hql,emp,startTime,endTime).listIterator().next();
		}
		return result.longValue();
	}
}
