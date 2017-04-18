package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.flyteas.ApartmentSys.Dao.StuAccessRecordDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.StuAccessRecord;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生门禁记录 数据访问实现 */
@Repository
public class StuAccessRecordDaoImpl implements StuAccessRecordDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@Override
	public StuAccessRecord getById(String id) 
	{
		return ht.get(StuAccessRecord.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuAccessRecord> getByStudent(Student student) 
	{
		String hql = "from StuAccessRecord where student = ?";
		return (List<StuAccessRecord>)ht.find(hql, student);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StuAccessRecord getLastByStudent(Student student,Apartment apartment) 
	{
		StuAccessRecord lastRec = null; //最后一条记录
		String hql = "from StuAccessRecord where student = ? and apartment = ? order by accessTime desc"; //按accessTime降序排序
		ht.setMaxResults(1); //设置获取记录的最大数 类似MySQL中的 limit
		List<StuAccessRecord> recList = (List<StuAccessRecord>) ht.find(hql, student,apartment);
		if(!recList.isEmpty()) //有记录
		{
			lastRec = recList.get(0);
		}
		ht.setMaxResults(0); //重设回获取所有记录，否则会对其他方法有影响
		return lastRec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuAccessRecord> getByApartment(Apartment apartment) 
	{
		String hql = "from StuAccessRecord where apartment = ?";
		return (List<StuAccessRecord>)ht.find(hql, apartment);
	}

	@Override
	public boolean add(StuAccessRecord stuAccessRecord) 
	{
		try
		{
			ht.save(stuAccessRecord);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(StuAccessRecord stuAccessRecord) 
	{
		try
		{
			ht.clear();
			ht.update(stuAccessRecord);
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
		StuAccessRecord stuAccessRecord = getById(id);
		if(stuAccessRecord == null)
		{
			return false;
		}
		try
		{
			ht.delete(stuAccessRecord);
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
		List<StuAccessRecord> recList = getByStudent(student);
		if(recList != null && !recList.isEmpty())
		{
			ht.deleteAll(recList);
			return recList.size();
		}
		return 0;
	}

	@Override
	public int delByApartment(Apartment apartment) 
	{
		List<StuAccessRecord> recList = getByApartment(apartment);
		if(recList != null && !recList.isEmpty())
		{
			ht.deleteAll(recList);
			return recList.size();
		}
		return 0;
	}

}
