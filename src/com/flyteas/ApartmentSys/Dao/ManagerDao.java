package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Manager;

/* 管理员 数据访问接口 */
public interface ManagerDao 
{
	public Manager managerLogin(String username,String password,String lastLoginIP); //管理员登陆，失败返回NULL
	public Manager getByUsername(String username); //通过用户名获取
	public List<Manager> getAll(); //获取所有用户
	public List<Manager> getAll(int page,int pageSize); //获取所有用户 分页
	public long getAllSize(); //获取所有用户数
	public List<Manager> findByUsername(String username); //通过用户名查找 模糊查找
	public List<Manager> findByRealName(String realName); //通过真实姓名查找 模糊查找
	public List<Manager> findByUnameRname(String keyword); //通过用户名或姓名 模糊查找
	public List<Manager> findByUnameRname(String keyword,int page,int pageSize); //通过用户名或姓名 模糊查找 分页
	public long findByUnameRnameSize(String keyword); //通过用户名或姓名 模糊查找 获取查找结果数量
	public boolean add(Manager manager); //添加
	public boolean saveModify(Manager manager); //保存修改
	public boolean delete(String username); //通过用户名删除
	public boolean checkPwd(String username,String password); //检查密码是否正确
}
