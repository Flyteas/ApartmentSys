package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Rota;

/* 值班信息 数据访问接口 */
public interface RotaDao 
{
	public Rota getById(String id); //通过ID获取
	public List<Rota> getByApartmentEmp(ApartmentEmp apartmentEmp); //根据公寓职工获取其所有值班信息，即获取某职工在某公寓楼的所有值班信息
	public List<Rota> getByApartment(Apartment apartment); //获取某公寓楼所有值班信息
	public List<Rota> getByEmployee(Employee employee); //获取某职工所有值班信息
	public List<Rota> findByAptTime(Apartment apartment,long startTime,long endTime, int page, int pageSize); //按时间检索公寓楼值班记录 -1表示时间无限制 分页
	public List<Rota> findByAptEmpTime(ApartmentEmp aptEmp, long startTime, long endTime,int page,int pageSize); //按时间检索公寓员工值班记录 -1表示时间无限制 分页
	public List<Rota> findByEmpTime(Employee emp, long startTime, long endTime,int page,int pageSize); //按时间检索员工值班记录 -1表示时间无限制 分页
	public long findByAptTimeSize(Apartment apartment,long startTime,long endTime); //按时间检索公寓楼值班记录数 -1表示时间无限制
	public long findByAptEmpTimeSize(ApartmentEmp aptEmp, long startTime, long endTime); //按时间检索公寓员工值班记录数 -1表示时间无限制
	public long findByEmpTimeSize(Employee emp, long startTime, long endTime); //按时间检索员工值班记录数 -1表示时间无限制
	public boolean add(Rota rota); //添加
	public boolean saveModify(Rota rota); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByApartmentEmp(ApartmentEmp apartmentEmp); //删除某职工在某公寓楼的所有值班信息
	public int delByApartment(Apartment apartment); //删除某公寓楼所有值班信息
	public int delByEmployee(Employee employee); //删除某职工所有值班信息
}
