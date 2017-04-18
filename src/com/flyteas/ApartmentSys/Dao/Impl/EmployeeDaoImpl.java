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

import com.flyteas.ApartmentSys.Dao.EmployeeDao;
import com.flyteas.ApartmentSys.Domain.Employee;

/* 职员 数据访问接口类 */
@Repository
public class EmployeeDaoImpl implements EmployeeDao
{
	private HibernateTemplate ht;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		ht = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Employee getByEmpId(String empId) 
	{
		return ht.get(Employee.class, empId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAll() 
	{
		String hql = "from Employee";
		return (List<Employee>)ht.find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findByEmpId(String empId) 
	{
		String hql = "from Employee where empId like ?";
		return (List<Employee>)ht.find(hql, "%"+empId+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findByEmpName(String name) 
	{
		String hql = "from Employee where name like ?";
		return (List<Employee>)ht.find(hql, "%"+name+"%");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findByEmpSex(int sex) 
	{
		String hql = "from Employee where sex = ?";
		return (List<Employee>)ht.find(hql, sex);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> findByEmpState(int state) 
	{
		String hql = "from Employee where state = ?";
		return (List<Employee>)ht.find(hql, state);
	}

	@Override
	public boolean add(Employee employee) 
	{
		try
		{
			ht.save(employee);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean saveModify(Employee employee) 
	{
		try
		{
			ht.clear();
			ht.update(employee);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean delete(String empId) 
	{
		Employee employee = getByEmpId(empId);
		if(employee == null)
		{
			return false;
		}
		try
		{
			ht.delete(employee);
		}
		catch(HibernateException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public long getAllSize() 
	{
		String hql = "select count(*) from Employee";
		Long result = (Long) ht.find(hql).listIterator().next();
		return result.intValue();
	}

	@Override
	public List<Employee> findByIdOrName(String keyword, int page, int pageSize) 
	{
		final String hql = "from Employee where empId like ? or name like ?";
		final String keywordParam = keyword;
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Employee> empList = ht.execute(new HibernateCallback<List<Employee>>()
		{
			public List<Employee> doInHibernate(Session session) throws HibernateException 
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
				List<Employee> empListRes = query.list();
				return empListRes;
			}
		});
		return empList;
	}

	@Override
	public long findByIdOrNameSize(String keyword) 
	{
		String hql = "select count(*) from Employee where empId like ? or name like ?";
		Long result = (Long) ht.find(hql,"%"+keyword+"%", "%"+keyword+"%").listIterator().next();
		return result.intValue();
	}

	@Override
	public List<Employee> getAll(int page, int pageSize) 
	{
		final String hql = "from Employee";
		final int pageParam = page;
		final int pageSizeParam = pageSize;
		List<Employee> empList = ht.execute(new HibernateCallback<List<Employee>>()
		{
			public List<Employee> doInHibernate(Session session) throws HibernateException 
			{
				Query query = session.createQuery(hql);
				query.setFirstResult((pageParam-1)*pageSizeParam); //计算分页起始位置
				if(pageSizeParam > 0)
				{
					query.setMaxResults(pageSizeParam); //分页大小
				}
				@SuppressWarnings("unchecked")
				List<Employee> empListRes = query.list();
				return empListRes;
			}
		});
		return empList;
	}
}
