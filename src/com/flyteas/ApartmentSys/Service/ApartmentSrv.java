package com.flyteas.ApartmentSys.Service;

import java.util.LinkedHashMap;
import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;

/* 公寓楼业务接口 */
public interface ApartmentSrv 
{
	public List<Apartment> getAllApartment(); //获取所有公寓楼
	public Apartment getApartment(String aptId); //根据ID获取公寓楼
	public List<Apartment> searchApartmentById(String id); //根据公寓楼ID搜索
	public List<Apartment> searchApartmentByName(String name); //根据公寓名字搜索
	public List<Apartment> searchApartmentByAddr(String address); //根据公寓地址搜索
	public List<Apartment> searchByNameOrAddr(String keyword,int page,int pageSize); //根据名字和地址搜索，分页  page为1 pageSize为0则返回所有记录
	public List<Apartment> searchByNameOrAddrEmpId(String empId,String keyword); //根据名字和地址搜索员工所属公寓楼
	public long searchByNameOrAddrSize(String keyword); //通过名字或地址 模糊查找 返回结果数量
	public boolean addApartment(Apartment apartment); //添加公寓楼
	public boolean modifyApartment(Apartment apartment); //修改公寓楼
	public boolean delApartment(String aptId); //删除公寓楼
	
	public Room getRoom(String roomId); //根据ID获取房间
	public List<Room> getRoomByAptId(String aptId); //根据公寓楼ID获取该楼所有房间 公寓楼不存在返回null
	public List<Room> getRoomNotFullByAptId(String aptId); //根据公寓楼ID获取该楼所有未满房间
	public List<Room> searchRoom(String aptId,String keyword,int page,int pageSize); //根据房间名或房间ID搜索某公寓楼 分页
	public long searchRoomSize(String aptId,String keyword); //搜索结果数
	public boolean addRoom(String aptId,Room room); //添加房间
	public boolean modifyRoom(Room room,int oldCap); //修改房间
	public boolean delRoom(String roomId); //删除房间
	
	public StuRoom getStuRoomById(String id); //根据ID获取住宿信息
	public List<StuRoom> getStuRoomByRoom(String roomId,int state); //获取房间住宿信息 state为住宿信息状态，0为获取正在住宿信息，1为已退住宿，2为所有住宿信息
	public int addStuRoom(StuRoom stuRoom); //添加住宿记录，成功返回0，如果state为0且房间人数已满，则返回-1 -2数据库错误
	public int modifyStuRoom(String id,int state,long startTime,long endTime); //修改住宿信息 0成功 -1房间人数已满 -2失败
	public boolean delStuRoom(String id); //删除住宿信息
	public int StuIn(String roomId,String stuId,long liveStartTime); //学生入住 0成功 -1为Room不存在 -2为Stu不存在 -3为房间满人 -4为床位使用中 -5数据库错误
	public int StuOut(String stuRoomId,long liveEndTime); //学生退住 0成功 -1记录不存在 -2记录已退房 -3数据库错误 
	public LinkedHashMap<StuRoom,String> getNoBackByApt(String aptId); //获取公寓楼未归寝信息 返回住宿信息和最后出门时间
	
	public List<Student> getCurrStuByRoom(String roomId); //获取某房间正在住宿的学生
	public List<Student> getAllStuByRoom(String roomId); //获取某房间所有住宿过的学生
	
	public ApartmentEmp getAptEmpById(String aptEmpId); //获取
	public ApartmentEmp getAptEmpByAptEmp(String aptId,String empId); //根据Apt和Emp获取
	public List<ApartmentEmp> getAptEmpByApt(String aptId,int empState,int page,int pageSize); //根据Apt和员工状态获取 分页
	public long getAptEmpByAptSize(String aptId,int empState); ////根据Apt和员工状态获取 结果数
	public List<ApartmentEmp> getAptEmpByEmp(String empId,int page,int pageSize); //根据Emp获取 分页
	public long getAptEmpByEmpSize(String empId); ////根据Emp获取 结果数
	public boolean addAptEmp(ApartmentEmp aptEmp); //添加
	public int delAptEmp(String aptEmpId); //删除 成功返回0 不存在-1 数据库失败-2
}
