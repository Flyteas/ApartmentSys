package com.flyteas.ApartmentSys.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.ManagerDao;
import com.flyteas.ApartmentSys.Domain.Manager;
import com.flyteas.ApartmentSys.Service.ManagerSrv;
import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 系统管理业务接口 */

@Service
public class ManagerSrvImpl implements ManagerSrv
{
	@Autowired
	private ManagerDao managerDao;
	
	@Transactional
	@Override
	public Manager login(String username, String password, String lastLoginIP) 
	{
		String pwdMD5 = MD5Encryptor.md5Encrypt(password); //对密码MD5进行摘要
		return managerDao.managerLogin(username, pwdMD5,lastLoginIP);
	}

	@Transactional
	@Override
	public boolean modifyProfile(Manager manager) 
	{
		return managerDao.saveModify(manager);
	}

	@Transactional
	@Override
	public int modifyPwd(Manager manager, String pwd, String newPwd) 
	{	
		String pwdMD5 = MD5Encryptor.md5Encrypt(pwd); //对密码MD5进行摘要
		if(!managerDao.checkPwd(manager.getUsername(), pwdMD5)) //原密码错误
		{
			return -1;
		}
		String newPwdMD5 = MD5Encryptor.md5Encrypt(newPwd); //对新密码进行MD5摘要
		manager.setPassword(newPwdMD5); //设置新密码
		if(managerDao.saveModify(manager)) //保存成功
		{
			return 0;
		}
		else
		{
			return -2; //数据库错误
		}
	}

	@Override
	public List<Manager> getAllManager() 
	{
		return managerDao.getAll();
	}

	@Override
	public List<Manager> findByUsername(String username) 
	{
		return managerDao.findByUsername(username);
	}

	@Override
	public List<Manager> findByRealName(String realName) 
	{
		return managerDao.findByRealName(realName);
	}

	@Transactional
	@Override
	public boolean add(String username,String password,int role,String realName,int sex,String phone) 
	{
		String pwdMD5 = MD5Encryptor.md5Encrypt(password); //对密码进行MD5
		Manager manager = new Manager(username,pwdMD5,role,realName,sex,phone);
		return managerDao.add(manager);
	}

	@Transactional
	@Override
	public boolean delByUsername(String username) 
	{
		return managerDao.delete(username);
	}

	@Override
	public List<Manager> findByUnameRname(String keyword) 
	{
		if(keyword.isEmpty()) //搜索关键字为空则返回所有记录
		{
			return managerDao.getAll();
		}
		return managerDao.findByUnameRname(keyword);
	}

	@Override
	public boolean checkUsernameUsed(String username) 
	{
		if(managerDao.getByUsername(username) != null) //用户名已使用
		{
			return true;
		}
		return false;
	}

	@Override
	public Manager getByUsername(String username) 
	{
		return managerDao.getByUsername(username);
	}

}
