package com.flyteas.ApartmentSys.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Service.EmployeeSrv;
import com.flyteas.ApartmentSys.Service.StudentSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 公寓控制器 */
@Controller
public class ApartmentCtrl 
{
	@Autowired
	private ApartmentSrv aptSrv;
	@Autowired
	private EmployeeSrv empSrv;
	@Autowired
	private StudentSrv stuSrv;
	
	private int pageSize = 20; //每页大小
	private int pageLength = 5; //翻页表长度
	
	private boolean loginCheck(HttpSession session) //检查是否处于已登录状态
	{
		Object manager;
		manager = session.getAttribute("user");
		if(manager == null)
		{
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = { "/Apartment.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = (String)request.getParameter("msg");
		if(msg != null) //传递消息
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("Apartment");
		return mv;
	}
	
	@RequestMapping(value = { "/Apartment.do" }, method = RequestMethod.POST)
	public ModelAndView searchApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //搜索公寓管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String nameOrAddr = (String)request.getParameter("nameOrAddr");
		if(nameOrAddr == null) //请求错误
		{
			mv.setViewName("Apartment");
			return mv;
		}
		/* 做分页 */
		String pageStr = (String)request.getParameter("page");
		int page = 1;
		if(pageStr != null && !pageStr.isEmpty()) //page有效
		{
			page = Integer.valueOf(pageStr);
			if(page <= 0) //页数非法
			{
				page = 1;
			}
		}
		long resultCount = aptSrv.searchByNameOrAddrSize(nameOrAddr); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Apartment> aparts = aptSrv.searchByNameOrAddr(nameOrAddr, page, pageSize); //搜索记录
		mv.addObject("nameOrAddr",nameOrAddr);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("aparts",aparts);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("Apartment");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("ApartmentAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg;
		String name = (String)request.getParameter("name");
		String address = (String)request.getParameter("address");
		if(name == null || address == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(name.isEmpty()) //公寓名为空
		{
			msg = "1"; //公寓名不能为空
			mv.addObject("msg",msg);
			mv.setViewName("ApartmentAdd");
			return mv;
		}
		Apartment newApt = new Apartment(name,address);
		if(aptSrv.addApartment(newApt)) //添加成功 跳转到修改页面
		{
			msg = "-1"; //添加成功
			mv.addObject("aptId",newApt.getId());
			mv.addObject("msg",msg);
			mv.setViewName("redirect:ApartmentModify.do");
		}
		else //添加失败
		{
			msg = "2"; //添加失败
			mv.addObject("msg",msg);
			mv.setViewName("ApartmentAdd");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改公寓 页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = (String)request.getParameter("aptId");
		String msg = (String)request.getParameter("msg");
		if(aptId == null || aptId.isEmpty()) //公寓ID空
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment modifyApt = aptSrv.getApartment(aptId);
		if(modifyApt == null) //公寓ID不正确
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		String createTimeStr = "";
		if( modifyApt.getCreateTime() >0 )
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr( modifyApt.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		int empCount = 0;
		List<Employee> empList = empSrv.getEmpByApt(aptId);
		if(empList != null)
		{
			empCount = empList.size();
		}
		mv.addObject("modifyApt", modifyApt);
		mv.addObject("empCount", empCount);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("ApartmentModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = (String)request.getParameter("aptId");
		String name = (String)request.getParameter("name");
		String address = (String)request.getParameter("address");
		String msg;
		if(aptId == null || name == null || address == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty()) //公寓ID空
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment modifyApt = aptSrv.getApartment(aptId);
		if(modifyApt == null) //公寓不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(!name.isEmpty())
		{
			modifyApt.setName(name);
			modifyApt.setAddress(address);
			if(aptSrv.modifyApartment(modifyApt)) //修改成功
			{
				msg = "0";
			}
			else
			{
				msg = "1";
			}
		}
		else //公寓名为空
		{
			msg = "2";
		}
		String createTimeStr = "";
		if(modifyApt.getCreateTime() >0 ) //如果存在创建时间信息，则显示
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyApt.getCreateTime(), "yyyy-MM-dd HH:mm:ss"); //转成字符串格式
		}
		int empCount = 0;
		List<Employee> empList = empSrv.getEmpByApt(aptId);
		if(empList != null)
		{
			empCount = empList.size();
		}
		mv.addObject("modifyApt",modifyApt);
		mv.addObject("empCount", empCount);
		mv.addObject("createTimeStr",createTimeStr);
		mv.addObject("msg",msg);
		mv.setViewName("ApartmentModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentDel.do" }, method = RequestMethod.GET)
	public ModelAndView delApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String msg;
		if(aptId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty() || aptSrv.getApartment(aptId) == null) //公寓ID为空或者公寓存在
		{
			msg = "1";
			mv.addObject("msg");
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		
		if(aptSrv.delApartment(aptId)) //删除成功
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		mv.setViewName("redirect:Apartment.do");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentRoom.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAptRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓房间管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = (String)request.getParameter("aptId");
		if(aptId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //aptId错误
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		String msg = (String)request.getParameter("msg");
		if(msg != null) //传递消息
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("apt",apt);
		mv.setViewName("ApartmentRoom");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentRoom.do" }, method = RequestMethod.POST)
	public ModelAndView searchAptRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓房间管理搜索
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = (String)request.getParameter("aptId");
		String idOrName = (String)request.getParameter("idOrName");
		if(aptId == null || idOrName == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //aptId错误
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		/* 做分页 */
		String pageStr = (String)request.getParameter("page");
		int page = 1;
		if(pageStr != null && !pageStr.isEmpty()) //page有效
		{
			page = Integer.valueOf(pageStr);
			if(page <= 0) //页数非法
			{
				page = 1;
			}
		}
		long resultCount = aptSrv.searchRoomSize(aptId, idOrName); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Room> rooms = aptSrv.searchRoom(aptId, idOrName, page, pageSize); //搜索记录
		mv.addObject("apt",apt);
		mv.addObject("idOrName",idOrName);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("rooms",rooms);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("ApartmentRoom");
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentSelect.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String selApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓选择搜索
	{
		request.setCharacterEncoding("UTF-8");
		String resultJson = "";
		String kw = request.getParameter("kw");
		String empId = request.getParameter("empId");
		String topParamStr = request.getParameter("topParam"); //返回前多少条
		if(kw == null || topParamStr == null) //非法请求
		{
			return resultJson;
		}
		int topParam = Integer.valueOf(topParamStr);
		if(loginCheck(session) && kw != null)
		{
			List<Apartment> aptList = new ArrayList<Apartment>();
			if(empId == null) //搜索所有公寓
			{
				if(topParam == -1)
				{
					aptList = aptSrv.searchByNameOrAddr(kw,1,0); //返回所有记录
				}
				else if(topParam > 0)
				{
					aptList = aptSrv.searchByNameOrAddr(kw,1,topParam); //返回所有记录
				}
			}
			else //搜索员工所属公寓
			{
				if(!empId.isEmpty())
				{
					aptList = aptSrv.searchByNameOrAddrEmpId(empId, kw);
				}
			}
			LinkedHashMap<String,List<Apartment>> resultMap = new LinkedHashMap<String,List<Apartment>>();
			resultMap.put("value", aptList);
			ObjectMapper objMapper = new ObjectMapper();
			resultJson = objMapper.writeValueAsString(resultMap);
		}
		return resultJson;
	}
	
	@RequestMapping(value = { "/RoomSelect.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String selRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间选择搜索
	{
		request.setCharacterEncoding("UTF-8");
		String resultJson = "";
		String kw = request.getParameter("kw");
		String aptId = request.getParameter("aptId");
		String topParamStr = request.getParameter("topParam"); //返回前多少条
		if(kw == null || aptId == null || topParamStr == null) //非法请求
		{
			return resultJson;
		}
		int topParam = Integer.valueOf(topParamStr);
		if(loginCheck(session) && kw != null && aptId != null)
		{
			List<Room> roomList = new ArrayList<Room>();
			if(topParam == -1) //返回所有记录
			{
				roomList = aptSrv.searchRoom(aptId, kw,1,0); //搜索房间名
			}
			else if(topParam > 0) //返回前n条
			{
				roomList = aptSrv.searchRoom(aptId, kw,1,topParam); //搜索房间名
			}
			LinkedHashMap<String,List<Room>> resultMap = new LinkedHashMap<String,List<Room>>();
			resultMap.put("value", roomList);
			ObjectMapper objMapper = new ObjectMapper();
			resultJson = objMapper.writeValueAsString(resultMap);
		}
		return resultJson;
	}
	
	@RequestMapping(value = { "/RoomAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //添加房间页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = (String)request.getParameter("aptId");
		if(aptId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		String msg = (String)request.getParameter("msg");
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("apt",apt);
		mv.setViewName("RoomAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //添加房间
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RoomAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "";
		String aptId = (String)request.getParameter("aptId");
		String name = (String)request.getParameter("name");
		String capacity = (String)request.getParameter("capacity");
		String price = (String)request.getParameter("price");
		if(aptId == null || name == null || capacity == null || price == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(name.isEmpty()) //房间名不能为空
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		if(capacity.isEmpty())
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		if(price.isEmpty())
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		Room room = new Room(name,apt,Integer.valueOf(capacity),Integer.valueOf(price));
		if(aptSrv.addRoom(aptId, room)) //添加成功
		{
			msg = "-1";
			mv.addObject("msg",msg);
			mv.addObject("roomId",room.getId());
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:RoomModify.do");
		}
		else
		{
			msg = "4";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
		}
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDel.do" }, method = RequestMethod.GET)
	public ModelAndView delRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //删除房间
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:ApartmentRoom.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = (String)request.getParameter("roomId");
		String aptId = (String)request.getParameter("aptId");
		if(roomId == null || aptId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		mv.addObject("aptId",aptId);
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			return mv;
		}
		String msg;
		if(aptSrv.delRoom(roomId)) //删除成功
		{
			msg = "0";
		}
		else
		{
			msg = "1";
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/RoomModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改房间页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String createTimeStr = "";
		String roomId = (String)request.getParameter("roomId");
		String aptId = (String)request.getParameter("aptId");
		if(aptId == null || roomId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(roomId.isEmpty())
		{
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:ApartmentRoom.do");
			return mv;
		}
		Room modifyRoom = aptSrv.getRoom(roomId);
		if(modifyRoom == null) //房间不存在
		{
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:ApartmentRoom.do");
			return mv;
		}
		if(modifyRoom.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyRoom.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
		}
		String msg = (String)request.getParameter("msg");
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("modifyRoom",modifyRoom);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("RoomModify");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改房间
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RoomModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "";
		String aptId = (String)request.getParameter("aptId");
		String roomId = (String)request.getParameter("roomId");
		String name = (String)request.getParameter("name");
		String capacity = (String)request.getParameter("capacity");
		String price = (String)request.getParameter("price");
		if(aptId == null || name == null || capacity == null || price == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(roomId.isEmpty()) //房间ID为空
		{
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:ApartmentRoom.do");
			return mv;
		}
		Room modifyRoom = aptSrv.getRoom(roomId);
		if(modifyRoom == null) //房间不存在
		{
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:ApartmentRoom.do");
			return mv;
		}
		String createTimeStr = "";
		if(modifyRoom.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyRoom.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(name.isEmpty()) //房间名不能为空
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.addObject("modifyRoom",modifyRoom);
			mv.addObject("createTimeStr",createTimeStr);
			return mv;
		}
		if(capacity.isEmpty())
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.addObject("modifyRoom",modifyRoom);
			mv.addObject("createTimeStr",createTimeStr);
			return mv;
		}
		if(Integer.valueOf(capacity) < modifyRoom.getCurrentAmout()) //人数容量小于当前人数
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.addObject("modifyRoom",modifyRoom);
			mv.addObject("createTimeStr",createTimeStr);
			return mv;
		}
		int oldCap = modifyRoom.getCapacity();
		modifyRoom.setCapacity(Integer.valueOf(capacity));
		modifyRoom.setName(name);
		modifyRoom.setPrice(Float.valueOf(price));
		if(aptSrv.modifyRoom(modifyRoom,oldCap)) //修改成功
		{
			msg = "0";
		}
		else
		{
			msg = "4";
		}
		mv.addObject("msg",msg);
		mv.addObject("modifyRoom",modifyRoom);
		mv.addObject("createTimeStr",createTimeStr);
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetail.do" }, method = RequestMethod.GET)
	public ModelAndView redirectRoomDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = request.getParameter("roomId");
		String msg = request.getParameter("msg");
		if(roomId == null || roomId.isEmpty())
		{
			mv.setViewName("404");
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		mv.addObject("room",room);
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("RoomDetail");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetail.do" }, method = RequestMethod.POST)
	public ModelAndView searchRoomDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿管理搜索
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = (String)request.getParameter("roomId");
		String state = (String)request.getParameter("state");
		if(roomId == null || state == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(roomId.isEmpty() || state.isEmpty() || (!state.equals("0") && !state.equals("1") && !state.equals("2")))
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		List<StuRoom> stuRooms = aptSrv.getStuRoomByRoom(roomId, Integer.valueOf(state));
		mv.addObject("room",room);
		mv.addObject("stuRooms",stuRooms);
		mv.addObject("state",state);
		mv.setViewName("RoomDetail");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetailAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectRoomDetailAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = (String)request.getParameter("roomId");
		if(roomId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(roomId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		mv.addObject("room",room);
		mv.setViewName("RoomDetailAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetailAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addRoomDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RoomDetailAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = (String)request.getParameter("roomId");
		String stuId = (String)request.getParameter("stuId");
		String state = (String)request.getParameter("state");
		String startTimeStr = (String)request.getParameter("startTime");
		String endTimeStr = (String)request.getParameter("endTime");
		String msg = "";
		long startTime = 0;
		long endTime = 0;
		if(roomId == null || stuId == null || state == null || startTimeStr == null || endTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(roomId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(stuId.isEmpty()) //学生选择为空
		{
			msg = "1";
			mv.addObject("room",room);
			mv.addObject("msg",msg);
			return mv;
		}
		Student stu = stuSrv.getById(stuId);
		if(stu == null) //学生不存在
		{
			msg = "2";
			mv.addObject("room",room);
			mv.addObject("msg",msg);
			return mv;
		}
		if(!state.equals("0") && !state.equals("1")) //状态错误
		{
			msg = "4";
			mv.addObject("room",room);
			mv.addObject("msg",msg);
			return mv;
		}
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty() && state.equals("1"))
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		StuRoom stuRoom = new StuRoom(room, stu, Integer.valueOf(state), startTime, endTime);
		int addResult = aptSrv.addStuRoom(stuRoom);
		if(addResult == 0) //添加成功
		{
			msg = "-1";
			mv.addObject("roomId",roomId);
			mv.addObject("detailId",stuRoom.getId());
			mv.addObject("msg",msg);
			mv.setViewName("redirect:RoomDetailModify.do");
			return mv;
		}
		else if(addResult == -1) //房间人数已满
		{
			msg = "3";
		}
		else //添加失败
		{
			msg = "4";
		}

		mv.addObject("room",room);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetailModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectRoomDetailModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿修改页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RoomDetailModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String roomId = (String)request.getParameter("roomId");
		String detailId = (String)request.getParameter("detailId");
		String msg = (String)request.getParameter("msg");
		if(roomId == null || detailId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(roomId.isEmpty() || detailId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		StuRoom stuRoom = aptSrv.getStuRoomById(detailId);
		if(stuRoom == null) //记录不存在
		{
			mv.addObject("roomId",roomId);
			mv.setViewName("redirect:RoomDetail.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg");
		}
		String startTimeStr = "";
		String endTimeStr = "";
		if(stuRoom.getLiveStartTime() > 0)
		{
			startTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveStartTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(stuRoom.getLiveEndTime() > 0)
		{
			endTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
		String createTimeStr = "";
		if(stuRoom.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		mv.addObject("startTimeStr",startTimeStr);
		mv.addObject("endTimeStr",endTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.addObject("stuRoom",stuRoom);
		mv.setViewName("RoomDetailModify");
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetailModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyRoomDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RoomDetailModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String detailId = (String)request.getParameter("detailId");
		String state = (String)request.getParameter("state");
		String startTimeStr = (String)request.getParameter("startTime");
		String endTimeStr = (String)request.getParameter("endTime");
		long startTime = 0;
		long endTime = 0;
		String msg = "";
		if(detailId == null || state == null || startTimeStr == null || endTimeStr == null) //非法请求 
		{
			mv.setViewName("404");
			return mv;
		}
		if(detailId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		StuRoom stuRoom = aptSrv.getStuRoomById(detailId);
		if(stuRoom == null) //记录不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(!state.equals("0") && !state.equals("1")) //状态错误
		{
			msg = "2";
			mv.addObject("detailId",stuRoom.getId());
			mv.addObject("roomId",stuRoom.getRoom().getId());
			mv.addObject("msg",msg);
			mv.setViewName("redirect:RoomDetailModify.do");
			return mv;
		}
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		int modifyResult = aptSrv.modifyStuRoom(detailId, Integer.valueOf(state), startTime, endTime);
		if(modifyResult == -1) //房间人数已满
		{
			msg = "1";
		}
		else if(modifyResult == -2) //修改失败
		{
			msg = "2";
		}
		else //修改成功
		{
			stuRoom = aptSrv.getStuRoomById(detailId);
			msg = "0";
		}
		if(stuRoom.getLiveStartTime() > 0)
		{
			startTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveStartTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(stuRoom.getLiveEndTime() > 0)
		{
			endTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
		String createTimeStr = "";
		if(stuRoom.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		mv.addObject("startTimeStr",startTimeStr);
		mv.addObject("endTimeStr",endTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.addObject("stuRoom",stuRoom);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/RoomDetailDel.do" }, method = RequestMethod.GET)
	public ModelAndView delRoomDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //房间住宿删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:RoomDetail.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String detailId = (String)request.getParameter("detailId");
		String roomId = (String)request.getParameter("roomId");
		String msg = "";
		if(detailId == null || roomId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		mv.addObject("roomId",roomId);
		StuRoom stuRoom = aptSrv.getStuRoomById(detailId);
		if(stuRoom == null) //记录不存在
		{
			return mv;
		}
		if(!aptSrv.delStuRoom(detailId)) //删除失败
		{
			msg = "1";
		}
		else //删除成功
		{
			msg = "0";
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentStuAcc.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAptStuAcc(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //查寝管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentStuAcc");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		if(aptId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null)
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		LinkedHashMap<StuRoom,String> noBackList = aptSrv.getNoBackByApt(aptId); //获取未归寝列表
		mv.addObject("apt",apt);
		mv.addObject("noBackListMap",noBackList);
		return mv;
	}
	
}
