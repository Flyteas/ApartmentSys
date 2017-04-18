package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Employee;

/* 职员 数据访问接口类 */
public interface EmployeeDao 
{
	public Employee getByEmpId(String empId); //通过工号获取 精确
	public List<Employee> getAll(); //获取所有
	public List<Employee> getAll(int page,int pageSize); //获取所有 分页
	public long getAllSize(); //获取所有记录数量
	public List<Employee> findByEmpId(String empId); //通过工号查找，模糊查找
	public List<Employee> findByEmpName(String name); //通过名字查找 模糊查找
	public List<Employee> findByIdOrName(String keyword,int page,int pageSize); //通过ID或名字查找 模糊查找 分页
	public long findByIdOrNameSize(String keyword); //通过ID或名字查找 模糊查找 结果数量
	public List<Employee> findByEmpSex(int sex); //通过性别查找
	public List<Employee> findByEmpState(int state); //根据职员状态查找，0为在职 1为离职
	public boolean add(Employee employee); //添加
	public boolean saveModify(Employee employee); //保存修改
	public boolean delete(String empId); //通过工号删除
}
