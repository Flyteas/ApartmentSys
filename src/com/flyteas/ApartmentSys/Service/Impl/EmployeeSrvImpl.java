package com.flyteas.ApartmentSys.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.ApartmentDao;
import com.flyteas.ApartmentSys.Dao.ApartmentEmpDao;
import com.flyteas.ApartmentSys.Dao.EmployeeDao;
import com.flyteas.ApartmentSys.Dao.RotaDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Rota;
import com.flyteas.ApartmentSys.Service.EmployeeSrv;

/* 职工业务实现  */
@Service
public class EmployeeSrvImpl implements EmployeeSrv
{
	@Autowired
	private EmployeeDao empDao;
	@Autowired
	private ApartmentDao aptDao;
	@Autowired
	private ApartmentEmpDao aptEmpDao;
	@Autowired
	private RotaDao rotaDao;

	@Override
	public List<Employee> getOnJobEmp() 
	{
		return empDao.findByEmpState(0);
	}

	@Override
	public Employee getById(String empId) 
	{
		return empDao.getByEmpId(empId);
	}

	@Override
	public List<Employee> searchById(String empId) 
	{
		return empDao.findByEmpId(empId);
	}

	@Override
	public List<Employee> searchByName(String name) 
	{
		return empDao.findByEmpName(name);
	}

	@Transactional
	@Override
	public int addEmp(Employee emp) 
	{
		if(empDao.getByEmpId(emp.getEmpId()) != null) //工号已存在
		{
			return -1;
		}
		if(empDao.add(emp)) //添加成功
		{
			return 0;
		}
		return -2;
	}

	@Transactional
	@Override
	public int modifyEmp(Employee emp) 
	{
		if(empDao.getByEmpId(emp.getEmpId()) == null) //员工不存在
		{
			return -1;
		}
		if(empDao.saveModify(emp)) //修改成功
		{
			return 0;
		}
		return -2;
	}

	@Transactional
	@Override
	public int delEmp(String empId) 
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null)
		{
			return -1;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByEmp(emp, 1, 0); //获取所有记录
		for(int i=0;i<aptEmpList.size();i++) //删除全部
		{
			ApartmentEmp aptEmp = aptEmpList.get(i);
			List<Rota> rotaList = rotaDao.getByApartmentEmp(aptEmp);
			for(int j=0;j<rotaList.size();j++) //删除全部
			{
				Rota rota = rotaList.get(j);
				rotaDao.delById(rota.getId());
			}
			aptEmpDao.delById(aptEmp.getId());
		}
		if(!empDao.delete(empId)) //删除失败
		{
			return -1;
		}
		return 0;
	}

	@Transactional
	@Override
	public int addEmpToApt(String empId, String aptId) 
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //员工不存在
		{
			return -1;
		}
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return -2;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByEmployee(emp); //获取某员工的所有公寓记录
		for(int i=0;i<aptEmpList.size();i++)
		{
			if(aptEmpList.get(i).getApartment().getId().equals(apt.getId())) //如果该员工已经在该公寓楼
			{
				return -3;
			}
		}
		ApartmentEmp aptEmp = new ApartmentEmp(emp,apt);
		if(!aptEmpDao.add(aptEmp)) //添加失败
		{
			return -4;
		}
		return 0;
	}

	@Transactional
	@Override
	public int addRota(String empId, String aptId, long startTime, long endTime) 
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //员工不存在
		{
			return -1;
		}
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return -2;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByEmployee(emp); //获取某员工的所有公寓记录
		for(int i=0;i<aptEmpList.size();i++)
		{
			ApartmentEmp aptEmp = aptEmpList.get(i);
			if(aptEmp.getApartment().getId().equals(apt.getId())) //如果该员工属于该公寓楼 则添加
			{
				Rota rota = new Rota(aptEmp,startTime,endTime);
				if(!rotaDao.add(rota)) //添加失败
				{
					return -4;
				}
				else
				{
					return 0; //成功
				}
			}
		}
		return -3;
	}

	@Transactional
	@Override
	public int modifyRota(String rotaId, String empId, String aptId, long startTime, long endTime) 
	{
		Rota rota = rotaDao.getById(rotaId);
		if(rota == null) //值班记录不存在
		{
			return -4;
		}
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //员工不存在
		{
			return -1;
		}
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return -2;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByEmployee(emp); //获取某员工的所有公寓记录
		for(int i=0;i<aptEmpList.size();i++)
		{
			ApartmentEmp aptEmp = aptEmpList.get(i);
			if(aptEmp.getApartment().getId().equals(apt.getId())) //如果该员工属于该公寓楼 则修改
			{
				rota.setApartmentEmp(aptEmp);
				rota.setStartTime(startTime);
				rota.setEndTime(endTime);
				if(!rotaDao.saveModify(rota)) //修改失败
				{
					return -5;
				}
				else
				{
					return 0; //成功
				}
			}
		}
		return -3;
	}


	@Override
	public List<Employee> getEmpByApt(String aptId) 
	{
		List<Employee> empList = new ArrayList<Employee>();
		Apartment apt = aptDao.getById(aptId);
		if(apt == null)
		{
			return null;
		}
		List<ApartmentEmp> aptEmpList =  aptEmpDao.getByApartment(apt);
		if(aptEmpList == null)
		{
			return null;
		}
		for(int i=0;i<aptEmpList.size();i++) //获取该公寓楼所有员工
		{
			Employee emp = aptEmpList.get(i).getEmployee();
			empList.add(emp);
		}
		return empList;
	}

	@Override
	public List<Employee> getOnJobEmpByApt(String aptId) 
	{
		List<Employee> empList = new ArrayList<Employee>();
		Apartment apt = aptDao.getById(aptId);
		if(apt == null)
		{
			return null;
		}
		List<ApartmentEmp> aptEmpList =  aptEmpDao.getByApartment(apt);
		if(aptEmpList == null)
		{
			return null;
		}
		for(int i=0;i<aptEmpList.size();i++) //获取该公寓楼所有在职员工
		{
			
			Employee emp = aptEmpList.get(i).getEmployee();
			if(emp.getState() == 0) //如果在职
			{
				empList.add(emp);
			}
		}
		return empList;
	}

	@Override
	public List<Employee> getAll() 
	{
		return empDao.getAll();
	}

	@Override
	public long getAllSize() 
	{
		return empDao.getAllSize();
	}

	@Override
	public List<Employee> getAll(int page, int pageSize) 
	{
		return empDao.getAll(page, pageSize);
	}

	@Override
	public List<Employee> searchByIdOrName(String keyword, int page, int pageSize)
	{
		if(keyword.isEmpty())
		{
			return empDao.getAll(page, pageSize);
		}
		return empDao.findByIdOrName(keyword, page, pageSize);
	}
	
	@Override
	public List<Employee> searchByIdOrNameAptId(String aptId,String keyword)
	{
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return null;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByApartment(apt);
		List<Employee> empList = new ArrayList<Employee>();
		for(int i=0;i<aptEmpList.size();i++) //检索关键字
		{
			Employee emp = aptEmpList.get(i).getEmployee();
			if(emp.getEmpId().contains(keyword) || emp.getName().contains(keyword) || emp.getPhone().contains(keyword)) //如果检索到
			{
				empList.add(emp); //添加到结果
			}
		}
		return empList;
	}

	@Override
	public long searchByIdOrNameSize(String keyword) 
	{
		if(keyword.isEmpty())
		{
			return empDao.getAllSize();
		}
		return empDao.findByIdOrNameSize(keyword);
	}

}
