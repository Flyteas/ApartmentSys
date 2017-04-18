package com.flyteas.ApartmentSys.Service.Impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flyteas.ApartmentSys.Dao.ApartmentDao;
import com.flyteas.ApartmentSys.Dao.ApartmentEmpDao;
import com.flyteas.ApartmentSys.Dao.EmployeeDao;
import com.flyteas.ApartmentSys.Dao.RoomDao;
import com.flyteas.ApartmentSys.Dao.RotaDao;
import com.flyteas.ApartmentSys.Dao.StuAccessRecordDao;
import com.flyteas.ApartmentSys.Dao.StuRoomDao;
import com.flyteas.ApartmentSys.Dao.StudentDao;
import com.flyteas.ApartmentSys.Dao.VisitRecordDao;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.Rota;
import com.flyteas.ApartmentSys.Domain.StuAccessRecord;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Domain.VisitRecord;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 公寓楼业务实现 */
@Transactional
@Service
public class ApartmentSrvImpl implements ApartmentSrv
{
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private StuRoomDao stuRoomDao;
	@Autowired
	private StudentDao stuDao;
	@Autowired
	private EmployeeDao empDao;
	@Autowired
	private ApartmentEmpDao aptEmpDao;
	@Autowired
	private StuAccessRecordDao stuAccDao;
	@Autowired
	private RotaDao rotaDao;
	@Autowired
	private VisitRecordDao visRecDao;
	
	@Override
	public List<Apartment> getAllApartment() 
	{
		return apartmentDao.getAll();
	}

	@Override
	public Apartment getApartment(String aptId) 
	{
		return apartmentDao.getById(aptId);
	}

	@Override
	public List<Apartment> searchApartmentById(String id) 
	{
		return apartmentDao.findById(id);
	}

	@Override
	public List<Apartment> searchApartmentByName(String name) 
	{
		return apartmentDao.findByName(name);
	}

	@Override
	public List<Apartment> searchApartmentByAddr(String address) 
	{
		return apartmentDao.findByAddr(address);
	}

	@Override
	public List<Apartment> searchByNameOrAddr(String keyword,int page,int pageSize)
	{
		if(keyword.isEmpty()) //返回所有
		{
			return apartmentDao.getAll(page,pageSize);
		}
		return apartmentDao.findByNameOrAddr(keyword,page,pageSize);
	}
	
	@Override
	public List<Apartment> searchByNameOrAddrEmpId(String empId,String keyword)
	{
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null) //员工不存在
		{
			return null;
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByEmployee(emp);
		List<Apartment> aptList = new ArrayList<Apartment>();
		for(int i=0;i<aptEmpList.size();i++) //遍历搜索公寓
		{
			Apartment apt = aptEmpList.get(i).getApartment();
			if(apt.getId().contains(keyword) || apt.getName().contains(keyword) || apt.getAddress().contains(keyword)) //如果包含搜索关键字
			{
				aptList.add(apt); //添加到结果列表
			}
		}
		return aptList;
	}
	
	@Override
	public boolean addApartment(Apartment apartment) 
	{
		return apartmentDao.add(apartment);
	}

	@Override
	public boolean modifyApartment(Apartment apartment) 
	{
		return apartmentDao.saveModify(apartment);
	}

	@Override
	public boolean delApartment(String aptId) 
	{
		Apartment apt = apartmentDao.getById(aptId);
		if(apt == null)
		{
			return false;
		}
		List<Room> roomList = roomDao.getByApartment(apt, 1, 0); //获取该公寓楼的所有房间
		for(int i=0;i<roomList.size();i++) //删除公寓所有房间
		{
			Room room = roomList.get(i);
			this.delRoom(room.getId());
		}
		List<ApartmentEmp> aptEmpList = aptEmpDao.getByApartment(apt); //获取该公寓楼所有员工信息
		for(int i=0;i<aptEmpList.size();i++) //删除公寓所有员工信息
		{
			ApartmentEmp aptEmp = aptEmpList.get(i);
			this.delAptEmp(aptEmp.getId());
		}
		stuAccDao.delByApartment(apt); //删除公寓门禁记录
		return apartmentDao.delete(aptId);
	}

	@Override
	public Room getRoom(String roomId) 
	{
		return roomDao.getById(roomId);
	}

	@Override
	public List<Room> getRoomByAptId(String aptId) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		return roomDao.getByApartment(apt,1,0);
	}

	@Override
	public List<Room> getRoomNotFullByAptId(String aptId) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		return roomDao.getNotFullRoomByApt(apt);
	}

	@Override
	public List<Room> searchRoom(String aptId, String keyword,int page,int pageSize) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return null;
		}
		if(keyword.isEmpty())
		{
			return roomDao.getByApartment(apt,page,pageSize);
		}
		return roomDao.findByAptRoom(apt, keyword,page,pageSize);
	}

	@Override
	public boolean addRoom(String aptId, Room room) 
	{
		Apartment apt = getApartment(aptId);
		if(apt == null)
		{
			return false;
		}
		room.setApartment(apt);
		return roomDao.add(room);
	}

	@Override
	public boolean modifyRoom(Room room,int oldCap) 
	{
		int newCap = room.getCapacity();
		Apartment apt = room.getApartment();
		apt.capacityAdd(newCap - oldCap); //更新公寓容量
		apartmentDao.saveModify(apt);
		return roomDao.saveModify(room);
	}

	@Override
	public boolean delRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return false;
		}
		List<StuRoom> stuRoomList = stuRoomDao.getAllByRoom(room);
		for(int i=0;i<stuRoomList.size();i++) //删除所有
		{
			StuRoom stuRoom = stuRoomList.get(i);
			this.delStuRoom(stuRoom.getId());
		}
		List<VisitRecord> visRecList = visRecDao.getByRoom(room);
		for(int i=0;i<visRecList.size();i++) //删除所有
		{
			VisitRecord visRec = visRecList.get(i);
			visRecDao.delById(visRec.getId());
		}
		return roomDao.delById(roomId);
	}

	@Override
	public List<StuRoom> getStuRoomByRoom(String roomId,int state) 
	{
		Room room = getRoom(roomId);
		if(room == null)
		{
			return null;
		}
		if(state == 0) //获取正在住宿信息
		{
			return stuRoomDao.getCurrByRoom(room);
		}
		else if(state == 1) //获取已退住宿信息
		{
			return stuRoomDao.getExitByRoom(room);
		}
		else if(state == 2) //获取所有住宿信息
		{
			return stuRoomDao.getAllByRoom(room);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public int StuIn(String roomId, String stuId, long liveStartTime) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return -1;
		}
		Student stu = stuDao.getByStuId(stuId);
		if(stu == null) //学生不存在
		{
			return -2;
		}
		if(room.isFull()) //房间满人
		{
			return -3;
		}
		StuRoom stuRoom = new StuRoom(room,stu,0,liveStartTime,0);
		if(!stuRoomDao.add(stuRoom)) //添加失败
		{
			return -5;
		}
		room.currAmoutInc(); //房间人数加1
		Apartment apt = room.getApartment();
		apt.usedCapAdd(1); //公寓人数+1
		apartmentDao.saveModify(apt);
		roomDao.saveModify(room);
		return 0;
	}

	@Override
	public int StuOut(String stuRoomId, long liveEndTime) 
	{
		StuRoom stuRoom = stuRoomDao.getById(stuRoomId);
		if(stuRoom == null) //住宿记录不存在
		{
			return -1;
		}
		if(stuRoom.getState() == 1) //该记录已经退房
		{
			return -2;
		}
		Room room = stuRoom.getRoom();
		if(!stuRoomDao.delById(stuRoomId)) //删除出错
		{
			return -3;
		}
		room.currAmoutDec(); //当前房间住宿人数减1
		Apartment apt = room.getApartment();
		apt.usedCapSub(1); //公寓人数-1
		apartmentDao.saveModify(apt);
		roomDao.saveModify(room);
		return 0;
	}

	@Override
	public List<Student> getCurrStuByRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return null;
		}
		return stuDao.getCurrByRoom(room);
	}

	@Override
	public List<Student> getAllStuByRoom(String roomId) 
	{
		Room room = roomDao.getById(roomId);
		if(room == null) //房间不存在
		{
			return null;
		}
		return stuDao.getAllByRoom(room);
	}

	@Override
	public long searchByNameOrAddrSize(String keyword) 
	{
		if(keyword.isEmpty())
		{
			return apartmentDao.getAllSize();
		}
		return apartmentDao.findByNameOrAddrSize(keyword);
	}

	@Override
	public long searchRoomSize(String aptId, String keyword) 
	{
		Apartment apt = apartmentDao.getById(aptId);
		if(apt == null) //aptId不存在
		{
			return 0;
		}
		if(keyword.isEmpty())
		{
			return roomDao.getByApartmentSize(apt);
		}
		return roomDao.findByAptRoomSize(apt, keyword);
	}

	@Override
	@Transactional
	public int addStuRoom(StuRoom stuRoom) 
	{
		if(stuRoom.getState() == 0 && stuRoom.getRoom().isFull()) //如果添加的是正在住宿信息且房间人数已满
		{
			return -1;
		}
		if(!stuRoomDao.add(stuRoom)) //添加失败
		{
			return -2;
		}
		if(stuRoom.getState() == 0)
		{
			Room room = stuRoom.getRoom();
			room.currAmoutInc(); //房间人数+1
			Apartment apt = room.getApartment();
			apt.usedCapAdd(1); //公寓人数+1
			apartmentDao.saveModify(apt);
			roomDao.saveModify(room);
		}
		return 0;
	}

	@Override
	public StuRoom getStuRoomById(String id) 
	{
		return stuRoomDao.getById(id);
	}

	@Override
	public int modifyStuRoom(String id, int state, long startTime, long endTime) 
	{
		StuRoom stuRoom = stuRoomDao.getById(id);
		if(stuRoom == null)
		{
			return -2;
		}
		int oldState = stuRoom.getState();
		int newState = state;
		if(oldState != newState) //状态改变
		{
			Room room = stuRoom.getRoom();
			Apartment apt = room.getApartment();
			if(newState == 1) //由正在住宿改为已退住宿
			{
				room.currAmoutDec(); //当前人数-1
				apt.usedCapSub(1); //公寓当前人数-1
			}
			else //由已退住宿改为正在住宿
			{
				if(room.isFull()) //人数已满
				{
					return -1;
				}
				room.currAmoutInc(); //人数+1
				apt.usedCapAdd(1); //公寓人数+1
			}
			apartmentDao.saveModify(apt);
			roomDao.saveModify(room);
		}
		if(state == 0)
		{
			endTime = 0; //退住时间清空
		}
		stuRoom.setState(state);
		stuRoom.setLiveStartTime(startTime);
		stuRoom.setLiveEndTime(endTime);
		if(!stuRoomDao.saveModify(stuRoom)) //修改失败
		{
			return -2;
		}
		return 0;
	}

	@Override
	public boolean delStuRoom(String id) 
	{
		StuRoom stuRoom = stuRoomDao.getById(id);
		if(stuRoom == null)
		{
			return false;
		}
		if(stuRoom.getState() == 0) //状态为正在住宿
		{
			Room room = stuRoom.getRoom();
			room.currAmoutDec(); //房间人数-1
			Apartment apt = room.getApartment();
			apt.usedCapSub(1); //公寓人数-1
			apartmentDao.saveModify(apt);
			roomDao.saveModify(room);
		}
		return stuRoomDao.delById(id);
	}

	@Override
	public ApartmentEmp getAptEmpById(String aptEmpId) 
	{
		return aptEmpDao.getById(aptEmpId);
	}

	@Override
	public ApartmentEmp getAptEmpByAptEmp(String aptId, String empId) 
	{
		if(aptId.isEmpty() || empId.isEmpty())
		{
			return null;
		}
		Apartment apt = apartmentDao.getById(aptId);
		Employee emp = empDao.getByEmpId(empId);
		if(apt == null || emp == null)
		{
			return null;
		}
		return aptEmpDao.getByAptEmp(apt, emp);
	}

	@Override
	public List<ApartmentEmp> getAptEmpByApt(String aptId,int empState,int page,int pageSize) 
	{
		if(aptId.isEmpty())
		{
			return null;
		}
		Apartment apt = apartmentDao.getById(aptId);
		if(apt == null)
		{
			return null;
		}
		return aptEmpDao.getByAptState(apt,empState,page,pageSize);
	}

	@Override
	public long getAptEmpByAptSize(String aptId, int empState) 
	{
		if(aptId.isEmpty())
		{
			return 0;
		}
		Apartment apt = apartmentDao.getById(aptId);
		if(apt == null)
		{
			return 0;
		}
		return aptEmpDao.getByAptStateSize(apt,empState);
	}

	@Override
	public List<ApartmentEmp> getAptEmpByEmp(String empId, int page,int pageSize) 
	{
		if(empId.isEmpty())
		{
			return null;
		}
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null)
		{
			return null;
		}
		return aptEmpDao.getByEmp(emp,page,pageSize);
	}

	@Override
	public long getAptEmpByEmpSize(String empId) 
	{
		if(empId.isEmpty())
		{
			return 0;
		}
		Employee emp = empDao.getByEmpId(empId);
		if(emp == null)
		{
			return 0;
		}
		return aptEmpDao.getByEmpSize(emp);
	}
	
	@Override
	public boolean addAptEmp(ApartmentEmp aptEmp) 
	{
		return aptEmpDao.add(aptEmp);
	}

	@Override
	public int delAptEmp(String aptEmpId) 
	{
		ApartmentEmp aptEmp = aptEmpDao.getById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			return -1;
		}
		List<Rota> rotaList = rotaDao.getByApartmentEmp(aptEmp); //获取值班记录
		for(int i=0;i<rotaList.size();i++) //删除全部
		{
			Rota rota = rotaList.get(i);
			rotaDao.delById(rota.getId());
		}
		if(!aptEmpDao.delById(aptEmpId)) //删除失败
		{
			return -2;
		}
		return 0;
	}

	@Override
	public LinkedHashMap<StuRoom,String> getNoBackByApt(String aptId) 
	{
		Apartment apt = apartmentDao.getById(aptId);
		if(apt == null)
		{
			return null;
		}
		List<StuRoom> stuRooms = stuRoomDao.getByApartment(apt);
		LinkedHashMap<StuRoom,String> noBackList = new LinkedHashMap<StuRoom,String>();
		for(int i=0;i<stuRooms.size();i++)
		{
			StuRoom stuRoom = stuRooms.get(i);
			if(stuRoom.getState() != 0) //不是正在住宿的学生则直接跳过
			{
				continue;
			}
			StuAccessRecord lastRec = stuAccDao.getLastByStudent(stuRoom.getStudent(),apt);
			if(lastRec == null || lastRec.getType() == 1) //没有任何记录或者最后一条记录是出门记录
			{
				String leaveTimeStr = "";
				if(lastRec != null)
				{
					try 
					{
					
						leaveTimeStr = DateTimeConverter.dateTimeLongToStr(lastRec.getAccessTime(), "yyyy-MM-dd HH:mm:ss");
					} 
					catch (Exception e) 
					{
						leaveTimeStr = "";
					}
				}
				noBackList.put(stuRoom, leaveTimeStr);
			}
		}
		return noBackList;
	}
}
