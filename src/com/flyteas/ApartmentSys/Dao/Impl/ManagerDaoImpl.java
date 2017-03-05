package com.flyteas.ApartmentSys.Dao.Impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.flyteas.ApartmentSys.Dao.ManagerDao;
import com.flyteas.ApartmentSys.Domain.Manager;

/* 系统管理员 数据访问实现 */
@Repository
public class ManagerDaoImpl implements ManagerDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Manager managerLogin(String username, String password, String lastLoginIP) 
	{
		String hql = "from Manager where username = ? and password = ?";
		List<Manager> managerList = (List<Manager>)ht.find(hql, username,password);
		if(managerList.isEmpty()) //如果查询结果为空，则说明用户名或者密码不正确
		{
			return null;
		}
		Manager manager = managerList.get(0); //获取第一个结果
		/* 拷贝一个副本，然后修改原本并保存到数据库 */
		Manager managerOld = new Manager();
		managerOld.setCreateTime(manager.getCreateTime());
		managerOld.setPassword(manager.getPassword());
		managerOld.setPhone(manager.getPhone());
		managerOld.setRealName(manager.getRealName());
		managerOld.setSex(manager.getSex());
		managerOld.setUsername(manager.getUsername());
		managerOld.setLastLoginIP(manager.getLastLoginIP());
		managerOld.setLastLoginTime(manager.getLastLoginTime());
		manager.setLastLoginIP(lastLoginIP);
		manager.setLastLoginTime(System.currentTimeMillis());
		saveModify(manager);
		return managerOld;
	}

	@Override
	public Manager getByUsername(String username) 
	{
		return ht.get(Manager.class, username);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Manager> findByUsername(String username) 
	{
		String hql = "from Manager where username like ?";
		return (List<Manager>)ht.find(hql, "%"+username+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Manager> findByRealName(String realName) 
	{
		String hql = "from Manager where realName like ?";
		return (List<Manager>)ht.find(hql, "%"+realName+"%");
	}

	@Override
	public boolean add(Manager manager) 
	{
		try
		{
			ht.save(manager);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Manager manager) 
	{
		try
		{
			ht.update(manager);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean delete(String username) 
	{
		Manager manager = getByUsername(username);
		if(manager == null)
		{
			return false;
		}
		try
		{
			ht.delete(manager);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Manager> getAll() 
	{
		String hql = "from Manager";
		return (List<Manager>)ht.find(hql);
	}

	@Override
	public boolean checkPwd(String username, String password) 
	{
		String hql = "from Manager where username = ? and password = ?";
		if(ht.find(hql, username,password).isEmpty()) //密码错误
		{
			return false;
		}
		return true;
	}
}
