package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.VisitRecord;

/* 访客记录 数据访问接口 */
public interface VisitRecordDao 
{
	public VisitRecord getById(String id); //id获取
	public List<VisitRecord> getByRoom(Room room); //获取某房间所有来访记录
	public List<VisitRecord> findById(String id); //ID查找 模糊
	public List<VisitRecord> findByVisitorName(String visitorName); //访客姓名查找 模糊
	public List<VisitRecord> findByIdCard(String visitorIdCard); //通过证件号码查找 模糊
	public List<VisitRecord> findByPhone(String phone); //电话查找 模糊
	public boolean add(VisitRecord visitRecord); //添加
	public boolean saveModify(VisitRecord visitRecord); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByRoom(Room room); //删除某房间所有访问记录
}
