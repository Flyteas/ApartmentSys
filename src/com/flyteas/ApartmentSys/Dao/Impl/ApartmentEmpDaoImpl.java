package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
}
