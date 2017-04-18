package com.flyteas.ApartmentSys.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.flyteas.ApartmentSys.Domain.Room;
import com.flyteas.ApartmentSys.Domain.VisitRecord;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Service.VisitorSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 访客控制器 */
@Controller
public class VisitRecordCtrl 
{
	@Autowired
	private ApartmentSrv aptSrv;
	@Autowired
	private VisitorSrv visSrv;
	
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
	
	@RequestMapping(value = { "/VisitRecord.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //访客管理页面
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
		mv.setViewName("VisitRecord");
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecord.do" }, method = RequestMethod.POST)
	public ModelAndView searchVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //搜索访客管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String nameOrPhone = (String)request.getParameter("nameOrPhone");
		if(nameOrPhone == null) //请求错误
		{
			mv.setViewName("VisitRecord");
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
		long resultCount = visSrv.searchByNameOrPhoneSize(nameOrPhone); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<VisitRecord> recs = visSrv.searchByNameOrPhone(nameOrPhone, page, pageSize); //搜索记录
		mv.addObject("nameOrPhone",nameOrPhone);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("recs",recs);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("VisitRecord");
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecordAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //访客添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("VisitRecordAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecordAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //访客添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("VisitRecordAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg;
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		String idCard = (String)request.getParameter("idCard");
		String roomId = (String)request.getParameter("roomId");
		String enterTimeStr = (String)request.getParameter("enterTime");
		String leaveTimeStr = (String)request.getParameter("leaveTime");
		long enterTime = 0;
		long leaveTime = 0;
		if(name == null || sex == null || phone == null || idCard == null || roomId == null || enterTimeStr == null || leaveTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(name.isEmpty()) //姓名为空
		{
			msg = "1"; //姓名不能为空
			mv.addObject("msg",msg);
			return mv;
		}
		if(roomId.isEmpty())
		{
			msg = "2"; //房间不能为空
			mv.addObject("msg",msg);
			return mv;
		}
		if(!(sex.equals("0") || sex.equals("1"))) //性别错误
		{
			msg = "4";
			mv.addObject("msg", msg);
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			msg = "3";
			mv.addObject("msg", msg);
			return mv;
		}
		if(!enterTimeStr.isEmpty())
		{
			try
			{
				enterTime = DateTimeConverter.dateTimeStrToLong(enterTimeStr, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				enterTime = 0;
			}
		}
		if(!leaveTimeStr.isEmpty())
		{
			try
			{
				leaveTime = DateTimeConverter.dateTimeStrToLong(leaveTimeStr, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				leaveTime = 0;
			}
		}
		VisitRecord visRec = new VisitRecord(name,Integer.valueOf(sex),phone,idCard,room, enterTime, leaveTime);
		int addResult = visSrv.addRec(visRec);
		if(addResult == 0) //添加成功
		{
			msg = "-1";
			mv.addObject("id",visRec.getId());
			mv.setViewName("redirect:VisitRecordModify.do");
		}
		else //添加失败
		{
			msg = "4";
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecordModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改访客 页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String id = (String)request.getParameter("id");
		String msg = (String)request.getParameter("msg");
		if(id == null || id.isEmpty()) //员工ID空
		{
			mv.setViewName("redirect:VisitRecord.do");
			return mv;
		}
		VisitRecord modifyRec = visSrv.getById(id);
		if(modifyRec == null) //记录ID不正确
		{
			mv.setViewName("redirect:VisitRecord.do");
			return mv;
		}
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		String enterTimeStr = "";
		String leaveTimeStr = "";
		String createTimeStr = "";
		if(modifyRec.getCreateTime() >0 )
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyRec.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(modifyRec.getEnterTime() > 0)
		{
			enterTimeStr = DateTimeConverter.dateTimeLongToStr(modifyRec.getEnterTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(modifyRec.getLeaveTime() > 0)
		{
			leaveTimeStr = DateTimeConverter.dateTimeLongToStr(modifyRec.getLeaveTime(), "yyyy-MM-dd HH:mm:ss");
		}

		mv.addObject("modifyRec", modifyRec);
		mv.addObject("enterTimeStr", enterTimeStr);
		mv.addObject("leaveTimeStr", leaveTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("VisitRecordModify");
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecordModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //访客记录修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:VisitRecordModify.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "0";
		String id = (String)request.getParameter("id");
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		String idCard = (String)request.getParameter("idCard");
		String roomId = (String)request.getParameter("roomId");
		String enterTimeStr = (String)request.getParameter("enterTime");
		String leaveTimeStr = (String)request.getParameter("leaveTime");
		long enterTime = 0;
		long leaveTime = 0;
		if(id == null || name == null || sex == null || phone == null || idCard == null || roomId == null || enterTimeStr == null || leaveTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(id.isEmpty()) //记录ID为空
		{
			mv.setViewName("redirect:VisitRecord.do");
			return mv;
		}
		if(name.isEmpty()) //姓名为空
		{
			msg = "1"; //姓名不能为空
			mv.addObject("msg",msg);
			return mv;
		}
		if(roomId.isEmpty())
		{
			msg = "2"; //房间不能为空
			mv.addObject("msg",msg);
			return mv;
		}
		if(!(sex.equals("0") || sex.equals("1"))) //性别错误
		{
			msg = "4";
			mv.addObject("msg", msg);
			return mv;
		}
		Room room = aptSrv.getRoom(roomId);
		if(room == null) //房间不存在
		{
			msg = "3";
			mv.addObject("msg", msg);
			return mv;
		}
		VisitRecord modifyRec = visSrv.getById(id);
		if(modifyRec == null) //记录ID不存在
		{
			mv.setViewName("redirect:VisitRecord.do");
			return mv;
		}
		if(!enterTimeStr.isEmpty())
		{
			try
			{
				enterTime = DateTimeConverter.dateTimeStrToLong(enterTimeStr, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				enterTime = 0;
			}
		}
		if(!leaveTimeStr.isEmpty())
		{
			try
			{
				leaveTime = DateTimeConverter.dateTimeStrToLong(leaveTimeStr, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				leaveTime = 0;
			}
		}
		modifyRec.setVisitorName(name);
		modifyRec.setVisitorSex(Integer.valueOf(sex));
		modifyRec.setVisitorIdCard(idCard);
		modifyRec.setVisitorPhone(phone);
		modifyRec.setVisitRoom(room);
		modifyRec.setEnterTime(enterTime);
		modifyRec.setLeaveTime(leaveTime);
		int modifyResult = visSrv.modifyRec(modifyRec);
		if(modifyResult == 0) //成功
		{
			msg = "0";
		}
		else
		{
			msg = "4";
		}
		mv.addObject("id",id);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/VisitRecordDel.do" }, method = RequestMethod.GET)
	public ModelAndView delVisRec(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //访客删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:VisitRecord.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		
		String id = request.getParameter("id");
		String msg;
		if(id == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(id.isEmpty() || visSrv.getById(id) == null) //记录ID为空或者记录不存在
		{
			msg = "1";
			mv.addObject("msg");
			return mv;
		}
		
		if(visSrv.delRec(id) == 0) //删除成功
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
}
