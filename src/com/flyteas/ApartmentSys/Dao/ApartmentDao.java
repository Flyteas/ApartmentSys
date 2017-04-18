package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;

/* 公寓楼 数据访问接口类 */
public interface ApartmentDao 
{
	public Apartment getById(String id); //通过ID获取Apartment
	public List<Apartment> getAll(); //获取所有
	public List<Apartment> getAll(int page,int pageSize); //获取所有 分页
	public List<Apartment> findById(String id); //通过ID查找 模糊查找
	public List<Apartment> findByName(String name); //通过name查找 模糊查找
	public List<Apartment> findByAddr(String address); //通过地址查找 模糊查找
	public List<Apartment> findByNameOrAddr(String keyword,int page,int pageSize); //通过名字或者地址查找，分页
	public boolean add(Apartment apartment); //添加
	public boolean saveModify(Apartment apartment); //保存修改
	public boolean delete(String id); //通过ID删除
	public long getAllSize(); //获取所有记录数
	public long findByNameOrAddrSize(String keyword); //获取搜索结果记录数
}
