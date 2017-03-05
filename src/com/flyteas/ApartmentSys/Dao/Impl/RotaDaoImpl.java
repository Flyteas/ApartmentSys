package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

}
