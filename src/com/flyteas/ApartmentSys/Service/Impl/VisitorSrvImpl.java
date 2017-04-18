package com.flyteas.ApartmentSys.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Dao.VisitRecordDao;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.VisitRecord;
import com.flyteas.ApartmentSys.Service.VisitorSrv;

/* 访客业务实现 */
@Service
public class VisitorSrvImpl implements VisitorSrv
{
	@Autowired
	private VisitRecordDao recDao;
	@Autowired
	private RoomDao roomDao;

	@Override
	public VisitRecord getById(String recId) 
	{
		return recDao.getById(recId);
	}

	@Override
	public List<VisitRecord> getByRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null)
		{
			return null;
		}
		return recDao.getByRoom(room);
	}

	@Override
	public List<VisitRecord> searchById(String recId) 
	{
		return recDao.findById(recId);
	}

	@Override
	public List<VisitRecord> searchByName(String name) 
	{
		return recDao.findByVisitorName(name);
	}

	@Override
	public List<VisitRecord> searchByIdCard(String idCard) 
	{
		return recDao.findByIdCard(idCard);
	}

	@Override
	public List<VisitRecord> searchByPhone(String phone) 
	{
		return recDao.findByPhone(phone);
	}

	@Transactional
	@Override
	public int addRec(VisitRecord rec) 
	{
		if(roomDao.getById(rec.getVisitRoom().getId()) == null) //房间不存在
		{
			return -1;
		}
		if(!recDao.add(rec)) //添加失败
		{
			return -2;
		}
		return 0;
	}

	@Transactional
	@Override
	public int modifyRec(VisitRecord rec) 
	{
		if(recDao.getById(rec.getId()) == null) //记录不存在
		{
			return -1;
		}
		if(roomDao.getById(rec.getVisitRoom().getId()) == null) //房间不存在
		{
			return -2;
		}
		if(!recDao.saveModify(rec)) //添加失败
		{
			return -3;
		}
		return 0;
	}

	@Transactional
	@Override
	public int delRec(String recId) 
	{
		VisitRecord rec = recDao.getById(recId);
		if(rec== null) //记录不存在
		{
			return -1;
		}
		if(!recDao.delById(recId)) //删除失败
		{
			return -2;
		}
		return 0;
	}

	@Transactional
	@Override
	public int leaveVisitor(String recId, long leaveTime) 
	{
		VisitRecord rec = recDao.getById(recId);
		if(rec == null)
		{
			return -1;
		}
		if(rec.getLeaveTime() != 0) //访客已离开
		{
			return -2;
		}
		rec.setLeaveTime(leaveTime);
		if(!recDao.saveModify(rec)) //保存失败
		{
			return -3;
		}
		return 0;
	}

	@Override
	public List<VisitRecord> searchByNameOrPhone(String keyword, int page,int pageSize) 
	{
		if(keyword.isEmpty())
		{
			return recDao.getAll(page, pageSize);
		}
		return recDao.findByNameOrPhone(keyword, page, pageSize);
	}

	@Override
	public long searchByNameOrPhoneSize(String keyword) 
	{
		if(keyword.isEmpty())
		{
			return recDao.getAllSize();
		}
		return recDao.findByNameOrPhoneSize(keyword);
	}

}
