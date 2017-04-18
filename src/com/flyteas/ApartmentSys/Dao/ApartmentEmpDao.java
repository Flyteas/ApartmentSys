package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;

/* 公寓职员 数据访问接口类 */
public interface ApartmentEmpDao 
{
	public ApartmentEmp getById(String id); //通过ID获取
	public ApartmentEmp getByAptEmp(Apartment apt,Employee emp); //通过Apt和Emp获取
	public List<ApartmentEmp> getByApartment(Apartment apartment); //获取某公寓楼所有职员
	public List<ApartmentEmp> getByAptState(Apartment apartment,int empState,int page,int pageSize); //获取公寓楼指定状态职员
	public long getByAptStateSize(Apartment apartment,int empState); //获取公寓楼指定状态职员 结果数
	public List<ApartmentEmp> getByEmp(Employee emp,int page,int pageSize); //获取员工公寓记录 分页
	public long getByEmpSize(Employee emp); //获取员工公寓记录 结果数
	public List<ApartmentEmp> getByEmployee(Employee employee); //获取某职员的所有工作公寓楼
	public boolean add(ApartmentEmp apartmentEmp); //添加
	public boolean saveModify(ApartmentEmp apartmentEmp); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByApartment(Apartment apartment); //删除某公寓楼的所有职员记录
	public int delByEmployee(Employee employee); //删除某职员的所有记录
	
}
