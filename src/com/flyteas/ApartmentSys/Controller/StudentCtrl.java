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
import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.StuRoom;
import com.flyteas.ApartmentSys.Domain.Student;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Service.StudentSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 学生控制器 */
@Controller
public class StudentCtrl 
{
	@Autowired
	private StudentSrv stuSrv;
	@Autowired
	private ApartmentSrv aptSrv;
	
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
	
	@RequestMapping(value = { "/Student.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生管理页面
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
		mv.setViewName("Student");
		return mv;
	}
	
	@RequestMapping(value = { "/Student.do" }, method = RequestMethod.POST)
	public ModelAndView searchStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //搜索学生管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuIdOrName = (String)request.getParameter("stuIdOrName");
		if(stuIdOrName == null) //请求错误
		{
			mv.setViewName("Student");
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
		long resultCount = stuSrv.searchByIdOrNameSize(stuIdOrName); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Student> stus = stuSrv.searchByIdOrName(stuIdOrName, page, pageSize); //搜索记录
		LinkedHashMap<Student,StuRoom> stusMap = new LinkedHashMap<Student,StuRoom>();
		for(int i=0;i<stus.size();i++) //查找每个学生当前住宿信息
		{
			Student stu = stus.get(i);
			List<StuRoom> stuCurrRoomList = stuSrv.getStuRoom(stu.getStuId(),0,1,0); //获取学生正在住宿信息
			StuRoom stuCurrRoom = null; //学生当前正在住宿的信息，不存在为null
			if(!stuCurrRoomList.isEmpty())
			{
				stuCurrRoom = stuCurrRoomList.get(0);
			}
			stusMap.put(stu, stuCurrRoom);
		}
		mv.addObject("stuIdOrName",stuIdOrName);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("stusMap",stusMap);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("Student");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("StudentAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "0";
		String stuId = (String)request.getParameter("stuId");
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		
		mv.setViewName("StudentAdd");
		if(stuId == null || name == null || sex == null || phone == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty()) //学号为空
		{
			msg = "1";
			mv.addObject("msg", msg);
			return mv;
		}
		if(name.isEmpty()) //姓名为空
		{
			msg = "3";
			mv.addObject("msg", msg);
			return mv;
		}
		if(!(sex.equals("0") || sex.equals("1"))) //性别错误
		{
			msg = "4";
			mv.addObject("msg", msg);
			return mv;
		}
		int addResult = stuSrv.addStu(stuId,name,Integer.valueOf(sex),phone);
		if(addResult == 0) //成功
		{
			msg = "-1";
			mv.addObject("stuId",stuId);
			mv.setViewName("redirect:StudentModify.do");
		}
		else if(addResult == -1) //学号存在
		{
			msg = "2";
		}
		else //未知错误
		{
			msg = "4";
		}
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改学生 页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = (String)request.getParameter("stuId");
		String msg = (String)request.getParameter("msg");
		if(stuId == null || stuId.isEmpty()) //学生ID空
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		Student modifyStu = stuSrv.getById(stuId);
		if(modifyStu == null) //学号不正确
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		String createTimeStr = "";
		if(modifyStu.getCreateTime() >0 )
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyStu.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		mv.addObject("modifyStu", modifyStu);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("StudentModify");
		return mv;
	}
	
	@RequestMapping(value = { "/StudentModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:StudentModify.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "0";
		String stuId = (String)request.getParameter("stuId");
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		if(stuId == null || name == null || sex == null || phone == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty()) //学号为空
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		if(name.isEmpty()) //姓名为空
		{
			msg = "2";
			mv.addObject("stuId",stuId);
			mv.addObject("msg", msg);
			return mv;
		}
		if(!(sex.equals("0") || sex.equals("1"))) //性别错误
		{
			msg = "1";
			mv.addObject("stuId",stuId);
			mv.addObject("msg", msg);
			return mv;
		}
		int modifyResult = stuSrv.modifyStu(stuId, name, Integer.valueOf(sex), phone);
		if(modifyResult == 0) //修改成功
		{
			msg = "0";
		}
		else if(modifyResult == -1) //学号不存在
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		else //修改失败
		{
			msg = "1";
		}
		mv.addObject("stuId",stuId);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentDel.do" }, method = RequestMethod.GET)
	public ModelAndView delStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:Student.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		
		String stuId = request.getParameter("stuId");
		String msg;
		if(stuId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty() || stuSrv.getById(stuId) == null) //学号为空或者学生不存在
		{
			msg = "1";
			mv.addObject("msg");
			return mv;
		}
		
		if(stuSrv.delStu(stuId) == 0) //删除成功
		{
			msg = "0";
		}
		else
		{
			msg = "1"; //删除失败
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentSelect.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String selApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓选择搜索
	{
		request.setCharacterEncoding("UTF-8");
		String resultJson = "";
		String kw = request.getParameter("kw");
		String topParamStr = request.getParameter("topParam"); //返回前多少条
		if(kw == null || topParamStr == null) //非法请求
		{
			return resultJson;
		}
		int topParam = Integer.valueOf(topParamStr);
		if(loginCheck(session) && kw != null)
		{
			List<Student> stuList = new ArrayList<Student>();
			if(topParam == -1) // -1返回所有
			{
				stuList = stuSrv.searchByIdOrName(kw, 1, 0); //返回所有记录
			}
			else if(topParam > 0) //返回前n条
			{
				stuList = stuSrv.searchByIdOrName(kw, 1, topParam); //返回前n记录
			}
			LinkedHashMap<String,List<Student>> resultMap = new LinkedHashMap<String,List<Student>>();
			resultMap.put("value", stuList);
			ObjectMapper objMapper = new ObjectMapper();
			resultJson = objMapper.writeValueAsString(resultMap);
		}
		return resultJson;
	}
	
	@RequestMapping(value = { "/StudentRoom.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStudentRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoom");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = request.getParameter("stuId");
		String msg = request.getParameter("msg");
		if(stuId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty())
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		Student stu = stuSrv.getById(stuId);
		if(stu == null) //学生不存在
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("stu",stu);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoom.do" }, method = RequestMethod.POST)
	public ModelAndView searchStudentRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoom");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = (String)request.getParameter("stuId");
		String state = (String)request.getParameter("state");
		if(stuId == null || state == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty() || state.isEmpty() || (!state.equals("0") && !state.equals("1") && !state.equals("2"))) //请求错误
		{
			mv.setViewName("404");
			return mv;
		}
		Student stu = stuSrv.getById(stuId);
		if(stu == null) //学生不存在
		{
			mv.setViewName("redirect:Student.do");
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
		long resultCount = stuSrv.getStuRoomSize(stuId, Integer.valueOf(state)); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<StuRoom> stuRooms = stuSrv.getStuRoom(stuId, Integer.valueOf(state), page, pageSize);
		mv.addObject("stu",stu);
		mv.addObject("stuRooms",stuRooms);
		mv.addObject("state",state);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoomAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStudentRoomAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoomAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = request.getParameter("stuId");
		if(stuId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty()) //请求错误
		{
			mv.setViewName("reidrect:Student.do");
			return mv;
		}
		Student stu = stuSrv.getById(stuId);
		if(stu == null) //学生不存在
		{
			mv.setViewName("reidrect:Student.do");
			return mv;
		}
		mv.addObject("stu",stu);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoomAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addStudentRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoomAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = request.getParameter("stuId");
		String aptId = request.getParameter("aptId");
		String roomId = request.getParameter("roomId");
		String state = request.getParameter("state");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		String msg = "";
		long startTime = 0;
		long endTime = 0;
		if(stuId == null || aptId == null || roomId == null || state == null || startTimeStr == null || endTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty())
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		Student stu = stuSrv.getById(stuId);
		if(stu == null) //学生不存在
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		mv.addObject("stu",stu);
		if(aptId.isEmpty()) //公寓ID为空
		{
			msg = "1";
			mv.addObject("msg",msg);
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓不存在
		{
			msg = "2";
			mv.addObject("msg",msg);
			return mv;
		}
		if(roomId.isEmpty()) //房间ID空
		{
			msg = "3";
			mv.addObject("msg",msg);
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			msg = "4";
			mv.addObject("msg",msg);
			return mv;
		}
		if(!state.equals("0") && !state.equals("1")) //参数错误
		{
			msg = "6";
			mv.addObject("msg",msg);
			return mv;
		}
		int stateInt = Integer.valueOf(state);
		if(stateInt == 0 && room.isFull()) //添加的是在住信息并且房间已满
		{
			msg = "5";
			mv.addObject("msg",msg);
			return mv;
		}
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(stateInt == 1 && !endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		StuRoom stuRoom = new StuRoom(room,stu,stateInt,startTime,endTime);
		if(aptSrv.addStuRoom(stuRoom) == 0) //添加成功
		{
			msg = "-1";
			mv.addObject("stuId",stu.getStuId());
			mv.addObject("stuRoomId",stuRoom.getId());
			mv.setViewName("redirect:StudentRoomModify.do");
		}
		else //添加失败
		{
			msg = "6";
			mv.addObject("msg",msg);
		}
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoomModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStudentRoomModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿修改页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoomModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = request.getParameter("stuId");
		String stuRoomId = request.getParameter("stuRoomId");
		String msg = request.getParameter("msg");
		if(stuId == null || stuRoomId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty())
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		if(stuRoomId.isEmpty())
		{
			mv.addObject("stuId",stuId);
			mv.setViewName("redirect:StudentRoom.do");
			return mv;
		}
		StuRoom stuRoom = aptSrv.getStuRoomById(stuRoomId);
		if(stuRoom == null) //记录不存在 
		{
			mv.addObject("stuId",stuId);
			mv.setViewName("redirect:StudentRoom.do");
			return mv;
		}
		String startTimeStr = "";
		String endTimeStr = "";
		String createTimeStr = "";
		if(stuRoom.getLiveStartTime() > 0)
		{
			startTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveStartTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(stuRoom.getLiveEndTime() > 0)
		{
			endTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getLiveEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(stuRoom.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(stuRoom.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("stu",stuRoom.getStudent());
		mv.addObject("stuRoom",stuRoom);
		mv.addObject("startTimeStr",startTimeStr);
		mv.addObject("endTimeStr",endTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoomModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyStudentRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentRoomModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuRoomId = request.getParameter("stuRoomId");
		String state = request.getParameter("state");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		String msg = "";
		long startTime = 0;
		long endTime = 0;
		if(stuRoomId == null || state == null || startTimeStr == null || endTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuRoomId.isEmpty())
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		StuRoom stuRoom = aptSrv.getStuRoomById(stuRoomId);
		if(stuRoom == null) //记录不存在
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		if(!state.equals("0") && !state.equals("1")) //参数错误
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.addObject("stuId",stuRoom.getStudent().getStuId());
			mv.addObject("stuRoomId",stuRoom.getId());
			mv.setViewName("redirect:StudentRoomModify.do");
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
		int modifyResult = aptSrv.modifyStuRoom(stuRoomId, Integer.valueOf(state), startTime, endTime);
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
		mv.addObject("stu",stuRoom.getStudent());
		mv.addObject("stuRoom",stuRoom);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/StudentRoomDel.do" }, method = RequestMethod.GET)
	public ModelAndView delStudentRoom(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //学生住宿删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:StudentRoom.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuRoomId = request.getParameter("stuRoomId");
		String stuId = request.getParameter("stuId");
		String msg = "";
		if(stuRoomId == null || stuId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty())
		{
			mv.setViewName("redirect:Student.do");
			return mv;
		}
		mv.addObject("stuId",stuId);
		if(stuRoomId.isEmpty())
		{
			return mv;
		}
		StuRoom stuRoom = aptSrv.getStuRoomById(stuRoomId);
		if(stuRoom == null) //记录不存在
		{
			return mv;
		}
		if(aptSrv.delStuRoom(stuRoomId)) //删除成功
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
	
	@RequestMapping(value = { "/StudentAccess.do" }, method = RequestMethod.GET)
	public ModelAndView redirectStudentAccess(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //门禁页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentAccess");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		return mv;
	}
	
	@RequestMapping(value = { "/StudentAccess.do" }, method = RequestMethod.POST)
	public ModelAndView addStudentAccess(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //门禁添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("StudentAccess");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String stuId = request.getParameter("stuId");
		String aptId = request.getParameter("aptId");
		String type = request.getParameter("type");
		String msg = "";
		if(stuId == null || aptId == null || type == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(stuId.isEmpty())
		{
			msg = "1";
			mv.addObject("msg",msg);
			return mv;
		}
		if(aptId.isEmpty())
		{
			msg = "3";
			mv.addObject("msg",msg);
			return mv;
		}
		if(type.isEmpty())
		{
			msg = "5";
			mv.addObject("msg",msg);
			return mv;
		}
		int addResult = stuSrv.addStuAccess(stuId, aptId, Integer.valueOf(type));
		switch(addResult)
		{
		case 0: //成功
			msg = "0";
			break;
		case -1: //学生不存在
			msg = "2";
			break;
		case -2: //公寓不存在
			msg = "4";
			break;
		case -3: //数据库错误
			msg = "5";
			break;
		}
		mv.addObject("msg",msg);
		return mv;
	}
}
