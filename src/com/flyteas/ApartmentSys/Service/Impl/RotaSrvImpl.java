package com.flyteas.ApartmentSys.Service.Impl;

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
import com.flyteas.ApartmentSys.Service.RotaSrv;

/* 值班业务实现 */
@Service
@Transactional
public class RotaSrvImpl implements RotaSrv
{
	@Autowired
	private RotaDao rotaDao;
	@Autowired
	private ApartmentDao aptDao;
	@Autowired
	private ApartmentEmpDao aptEmpDao;
	@Autowired
	private EmployeeDao empDao;
	
	@Override
	public Rota getById(String rotaId) 
	{
		return rotaDao.getById(rotaId);
	}

	@Override
	public boolean addRota(Rota rota) 
	{
		return rotaDao.add(rota);
	}

	@Override
	public int modifyRota(Rota rota, long startTime, long endTime) 
	{
		if(startTime <= 0)
		{
			return -1;
		}
		if(endTime <= 0)
		{
			return -2;
		}
		if(startTime > endTime)
		{
			return -3;
		}
		rota.setStartTime(startTime);
		rota.setEndTime(endTime);
		if(!rotaDao.saveModify(rota)) //保存失败
		{
			return -4;
		}
		return 0;
	}

	@Override
	public int delRota(String rotaId) 
	{
		Rota rota = rotaDao.getById(rotaId);
		if(rota == null)
		{
			return -1;
		}
		if(!rotaDao.delById(rotaId)) //删除失败
		{
			return -2;
		}
		return 0;
	}

	@Override
	public List<Rota> searchByAptTime(String aptId, long startTime,long endTime, int page, int pageSize) 
	{
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return null;
		}
		return rotaDao.findByAptTime(apt, startTime, endTime, page, pageSize);
	}

	@Override
	public long searchByAptTimeSize(String aptId, long startTime,long endTime) 
	{
		Apartment apt = aptDao.getById(aptId);
		if(apt == null) //公寓楼不存在
		{
			return -1;
		}
		return rotaDao.findByAptTimeSize(apt, startTime, endTime);
	}

	@Override
	public List<Rota> searchByAptEmpTime(String aptEmpId, long startTime, long endTime, int page, int pageSize) 
	{
		ApartmentEmp aptEmp = aptEmpDao.getById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			return null;
		}
		return rotaDao.findByAptEmpTime(aptEmp, startTime, endTime,page,pageSize);
	}

	@Override
	public long searchByAptEmpTimeSize(String aptEmpId, long startTime, long endTime) 
	{
		ApartmentEmp aptEmp = aptEmpDao.getById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			return -1;
		}
		return rotaDao.findByAptEmpTimeSize(aptEmp, startTime, endTime);
	}
	
	@Override
	public List<Rota> searchByEmpTime(String empId, long startTime, long endTime, int page, int pageSize) 
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //不存在
		{
			return null;
		}
		return rotaDao.findByEmpTime(emp, startTime, endTime,page,pageSize);
	}

	@Override
	public long searchByEmpTimeSize(String empId, long startTime, long endTime) 
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //不存在
		{
			return -1;
		}
		return rotaDao.findByEmpTimeSize(emp, startTime, endTime);
	}
}
