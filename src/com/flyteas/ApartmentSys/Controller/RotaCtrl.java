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

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Domain.Rota;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Service.EmployeeSrv;
import com.flyteas.ApartmentSys.Service.RotaSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 值班控制器 */
@Controller
public class RotaCtrl 
{
	@Autowired
	private RotaSrv rotaSrv;
	@Autowired
	private ApartmentSrv aptSrv;
	@Autowired
	private EmployeeSrv empSrv;
	
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
	
	@RequestMapping(value = { "/ApartmentRota.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartmentRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓值班管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String msg = request.getParameter("msg");
		if(aptId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("apt",apt);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentRota.do" }, method = RequestMethod.POST)
	public ModelAndView listApartmentRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓值班管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		long startTime = -1;
		long endTime = -1;
		if(aptId == null || startTimeStr == null || endTimeStr == null) //非法请求
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
		if(apt == null) //公寓不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		mv.addObject("apt",apt);
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
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
		long resultCount = rotaSrv.searchByAptTimeSize(aptId, startTime, endTime); //获取查询记录数
		if(resultCount < 0) //公寓楼不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Rota> rotas = rotaSrv.searchByAptTime(aptId, startTime, endTime, page, pageSize); //搜索记录
		mv.addObject("startTime",startTimeStr);
		mv.addObject("endTime",endTimeStr);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("rotas",rotas);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentRotaAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartmentRotaAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓值班添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentRotaAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String msg = request.getParameter("msg");
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
		if(apt == null) //不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("apt",apt);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpRota.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartmentEmpRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工值班管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmpRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = request.getParameter("msg");
		if(aptEmpId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("aptEmp",aptEmp);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpRota.do" }, method = RequestMethod.POST)
	public ModelAndView listApartmentEmpRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工值班管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmpRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptEmpId = request.getParameter("aptEmpId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		long startTime = -1;
		long endTime = -1;
		if(aptEmpId == null || startTimeStr == null || endTimeStr == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		mv.addObject("aptEmp",aptEmp);
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
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
		long resultCount = rotaSrv.searchByAptEmpTimeSize(aptEmpId, startTime, endTime); //获取查询记录数
		if(resultCount < 0) //不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Rota> rotas = rotaSrv.searchByAptEmpTime(aptEmpId, startTime, endTime, page, pageSize); //搜索记录
		mv.addObject("startTime",startTimeStr);
		mv.addObject("endTime",endTimeStr);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("rotas",rotas);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpRotaAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectApartmentEmpRotaAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工值班添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmpRotaAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = request.getParameter("msg");
		if(aptEmpId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("aptEmp",aptEmp);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeRota.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmployeeRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工值班管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		String msg = request.getParameter("msg");
		if(empId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty())
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		Employee emp = empSrv.getById(empId);
		if(emp == null) //员工不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("emp",emp);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeRota.do" }, method = RequestMethod.POST)
	public ModelAndView listEmployeeRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工值班管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		long startTime = -1;
		long endTime = -1;
		if(empId == null || startTimeStr == null || endTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Employee emp = empSrv.getById(empId);
		if(emp == null) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		mv.addObject("emp",emp);
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
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
		long resultCount = rotaSrv.searchByEmpTimeSize(empId, startTime, endTime); //获取查询记录数
		if(resultCount < 0) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Rota> rotas = rotaSrv.searchByEmpTime(empId, startTime, endTime, page, pageSize); //搜索记录
		mv.addObject("startTime",startTimeStr);
		mv.addObject("endTime",endTimeStr);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("rotas",rotas);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeRotaAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmployeeRotaAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工值班添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeRotaAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		String msg = request.getParameter("msg");
		if(empId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		Employee emp = empSrv.getById(empId);
		if(emp == null) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("emp",emp);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptRota.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmployeeAptRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓值班管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeAptRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = request.getParameter("msg");
		if(empId == null || aptEmpId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.addObject("empId",empId);
			mv.setViewName("redirect:EmployeeApt.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.addObject("empId",empId);
			mv.setViewName("redirect:EmployeeApt.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("aptEmp",aptEmp);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptRota.do" }, method = RequestMethod.POST)
	public ModelAndView listEmployeeAptRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓值班管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeAptRota");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptEmpId = request.getParameter("aptEmpId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		long startTime = -1;
		long endTime = -1;
		if(aptEmpId == null || startTimeStr == null || endTimeStr == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		mv.addObject("aptEmp",aptEmp);
		if(!startTimeStr.isEmpty())
		{
			startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
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
		long resultCount = rotaSrv.searchByAptEmpTimeSize(aptEmpId, startTime, endTime); //获取查询记录数
		if(resultCount < 0) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Rota> rotas = rotaSrv.searchByAptEmpTime(aptEmpId, startTime, endTime, page, pageSize); //搜索记录
		mv.addObject("startTime",startTimeStr);
		mv.addObject("endTime",endTimeStr);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("rotas",rotas);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptRotaAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmployeeAptRotaAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓值班添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeAptRotaAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = request.getParameter("msg");
		if(aptEmpId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpById(aptEmpId);
		if(aptEmp == null) //不存在
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(msg != null)
		{
			mv.addObject("msg",msg);
		}
		mv.addObject("aptEmp",aptEmp);
		return mv;
	}
	
	@RequestMapping(value = { "/RotaAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //值班添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String empId = request.getParameter("empId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		if(aptId == null || empId == null || startTimeStr == null || endTimeStr == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		mv.setViewName("redirect:index.jsp");
		if(aptId.isEmpty() || empId.isEmpty())
		{
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpByAptEmp(aptId, empId);
		if(aptEmp == null) //不存在
		{
			return mv;
		}
		long startTime = 0;
		long endTime = 0;
		if(!startTimeStr.isEmpty())
		{
			startTime =  DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(!endTimeStr.isEmpty())
		{
			endTime =  DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		if(startTime < 0 || endTime < 0 || startTime > endTime) //时间错误
		{
			return mv;
		}
		Rota rota = new Rota(aptEmp,startTime,endTime);
		if(!rotaSrv.addRota(rota)) //添加失败
		{
			return mv;
		}
		mv.addObject("rotaId",rota.getId());
		mv.setViewName("redirect:RotaModify.do");
		return mv;
	}
	
	@RequestMapping(value = { "/RotaModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectRotaModify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //值班修改页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("RotaModify");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String rotaId = request.getParameter("rotaId");
		String msg = request.getParameter("msg");
		if(rotaId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(rotaId.isEmpty())
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		Rota rota = rotaSrv.getById(rotaId);
		if(rota == null) //不存在
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String startTimeStr = "";
		String endTimeStr = "";
		String createTimeStr = "";
		if(rota.getStartTime() > 0)
		{
			startTimeStr = DateTimeConverter.dateTimeLongToStr(rota.getStartTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(rota.getEndTime() > 0)
		{
			endTimeStr = DateTimeConverter.dateTimeLongToStr(rota.getEndTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(rota.getCreateTime() > 0)
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(rota.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(msg != null)
		{
			mv.addObject("msg");
		}
		mv.addObject("startTimeStr",startTimeStr);
		mv.addObject("endTimeStr",endTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.addObject("rota",rota);
		return mv;
	}

	@RequestMapping(value = { "/RotaModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //值班修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:RotaModify.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String rotaId = request.getParameter("rotaId");
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");
		String msg = "";
		long startTime = -1;
		long endTime = -1;
		if(rotaId == null || startTimeStr == null || endTimeStr == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(rotaId.isEmpty())
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		Rota rota = rotaSrv.getById(rotaId);
		if(rota == null)
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.addObject("rotaId",rota.getId());
		if(startTimeStr.isEmpty()) //开始时间空
		{
			msg = "1";
			mv.addObject("msg",msg);
			return mv;
		}
		if(endTimeStr.isEmpty()) //结束时间空
		{
			msg = "2";
			mv.addObject("msg",msg);
			return mv;
		}
		startTime = DateTimeConverter.dateTimeStrToLong(startTimeStr, "yyyy-MM-dd HH:mm:ss");
		endTime = DateTimeConverter.dateTimeStrToLong(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		if(startTime < 0 || endTime < 0)
		{
			msg = "4";
			mv.addObject("msg",msg);
			return mv;
		}
		if(startTime > endTime) //开始时间大于结束时间
		{
			msg = "3";
			mv.addObject("msg",msg);
			return mv;
		}
		if(rotaSrv.modifyRota(rota, startTime, endTime) != 0) //保存失败
		{
			msg = "4";
		}
		else
		{
			msg = "0";
		}
		mv.addObject("msg",msg);
		return mv;
	}
	

	@RequestMapping(value = { "/RotaDel.do" }, method = RequestMethod.GET)
	public ModelAndView delRota(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //值班删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String rotaId = request.getParameter("rotaId");
		String type = request.getParameter("type"); //通过type来识别删除请求来源
		String msg = "";
		if(rotaId == null || type == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(rotaId.isEmpty() || (!type.equals("0") && !type.equals("1") && !type.equals("2") && !type.equals("3")))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		Rota rota = rotaSrv.getById(rotaId);
		if(rota == null) //不存在
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		switch(type)
		{
		case "0": //删除请求来自ApartmentEmpRota
			mv.addObject("aptEmpId",rota.getApartmentEmp().getId());
			mv.setViewName("redirect:ApartmentEmpRota.do");
			break;
		case "1": //删除请求来自EmployeeAptRota
			mv.addObject("aptEmpId",rota.getApartmentEmp().getId());
			mv.setViewName("redirect:EmployeeAptRota.do");
			break;
		case "2": //删除请求来自ApartmentRota
			mv.addObject("aptId",rota.getApartmentEmp().getApartment().getId());
			mv.setViewName("redirect:ApartmentRota.do");
			break;
		case "3": //删除请求来自EmployeeRota
			mv.addObject("empId",rota.getApartmentEmp().getEmployee().getEmpId());
			mv.setViewName("redirect:EmployeeRota.do");
			break;
		}
		if(rotaSrv.delRota(rotaId) != 0) //删除失败
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
}
