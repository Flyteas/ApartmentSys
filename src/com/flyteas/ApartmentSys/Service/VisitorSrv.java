package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.VisitRecord;

/* 访客业务接口 */
public interface VisitorSrv 
{
	public VisitRecord getById(String recId); //根据id获取
	public List<VisitRecord> getByRoom(String roomId); //根据房间获取访客记录
	public List<VisitRecord> searchById(String recId); //根据id搜索
	public List<VisitRecord> searchByName(String name); //根据名字搜索
	public List<VisitRecord> searchByIdCard(String idCard); //根据证件号搜索
	public List<VisitRecord> searchByPhone(String phone); //根据电话搜索
	public int addRec(VisitRecord rec); //添加访客记录 0成功 -1房间不存在 -2数据库错误
	public int modifyRec(VisitRecord rec); //修改访客记录 0成功 -1记录不存在 -2房间不存在 -3数据库错误
	public int delRec(String recId); //删除访客记录 0成功 -1记录不存在 -2数据库错误
	public int leaveVisitor(String recId,long leaveTime); //访客离开 0成功  -1记录不存在 -2访客记录对应访客已离开 -3数据库错误
}
