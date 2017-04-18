package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Employee;

/* 职工业务接口 */
public interface EmployeeSrv 
{
	public List<Employee> getAll(); //获取所有员工
	public long getAllSize(); //获取所有记录数
	public List<Employee> getAll(int page,int pageSize); //获取所有员工 分页 page为1 pageSize为0则返回所有记录
	public List<Employee> getEmpByApt(String aptId); //获取某公寓楼员工
	public List<Employee> getOnJobEmp(); //获取所有在职员工
	public List<Employee> getOnJobEmpByApt(String aptId); //获取某公寓楼在职员工
	public Employee getById(String empId); //根据工号获取
	public List<Employee> searchById(String empId); //通过工号搜索
	public List<Employee> searchByName(String name); //通过名字搜索
	public List<Employee> searchByIdOrName(String keyword,int page,int pageSize); //通过工号或名字搜索 分页 page为1 pageSize为0则返回所有记录
	public List<Employee> searchByIdOrNameAptId(String aptId,String keyword); //通过工号或名字搜索公寓员工
	public long searchByIdOrNameSize(String keyword); //获取搜索结果大小
	public int addEmp(Employee emp); //添加员工 0成功 -1工号已存在 -2数据库错误
	public int modifyEmp(Employee emp); //修改员工 0成功 -1员工不存在 -2数据库错误
	public int delEmp(String empId); //删除员工 0成功 -1员工不存在 -2数据库错误
	public int addEmpToApt(String empId,String aptId); //添加员工到公寓楼 0成功 -1员工不存在 -2公寓楼不存在 -3员工已经在该公寓楼 -4数据库错误
	public int addRota(String empId,String aptId,long startTime,long endTime); //添加值班信息 0成功 -1员工不存在 -2公寓楼不存在 -3员工不属于该公寓楼 -4数据库错误
	public int modifyRota(String rotaId,String empId,String aptId,long startTime,long endTime); //修改值班信息 0成功 -1员工不存在 -2公寓楼不存在 -3员工不属于该公寓楼 -4值班信息不存在 -5数据库错误
}
