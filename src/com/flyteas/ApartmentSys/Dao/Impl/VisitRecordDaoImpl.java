package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

}
