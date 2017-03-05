package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Manager;

/* 系统管理业务接口 */
public interface ManagerSrv 
{
	public Manager login(String username,String password,String lastLoginIP); //登陆业务，登陆失败返回null
	public boolean modifyProfile(Manager manager); //修改管理员个人资料
	public int modifyPwd(Manager manager,String pwd,String newPwd); //修改密码 0修改成功 -1为原密码错误 -2为数据库错误
	public List<Manager> getAllManager(); //获取所有管理员
	public List<Manager> findByUsername(String username); //通过用户名查找 模糊查找
	public List<Manager> findByRealName(String realName); //通过真实姓名查找 模糊查找
	public boolean add(Manager manager); //添加新管理员
	public boolean delByUsername(String username); //根据用户名删除管理员
}