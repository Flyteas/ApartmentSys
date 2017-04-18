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
import com.flyteas.ApartmentSys.Domain.ApartmentEmp;
import com.flyteas.ApartmentSys.Domain.Employee;
import com.flyteas.ApartmentSys.Service.ApartmentSrv;
import com.flyteas.ApartmentSys.Service.EmployeeSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 员工控制器 */
@Controller
public class EmployeeCtrl 
{
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
	
	@RequestMapping(value = { "/Employee.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工管理页面
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
		mv.setViewName("Employee");
		return mv;
	}
	
	@RequestMapping(value = { "/Employee.do" }, method = RequestMethod.POST)
	public ModelAndView searchEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //搜索员工管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empIdOrName = (String)request.getParameter("empIdOrName");
		if(empIdOrName == null) //请求错误
		{
			mv.setViewName("Employee");
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
		long resultCount = empSrv.searchByIdOrNameSize(empIdOrName); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Employee> emps = empSrv.searchByIdOrName(empIdOrName, page, pageSize); //搜索记录
		mv.addObject("empIdOrName",empIdOrName);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("emps",emps);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("Employee");
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("EmployeeAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "0";
		String empId = (String)request.getParameter("empId");
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		String state = (String)request.getParameter("state");
		String inductionTime = (String)request.getParameter("inductionTime");
		String departureTime = (String)request.getParameter("departureTime");
		long indTime = 0;
		long depTime = 0;
		
		mv.setViewName("EmployeeAdd");
		if(empId == null || name == null || sex == null || phone == null || state == null || inductionTime == null || departureTime == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty()) //工号为空
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
		if(!(sex.equals("0") || sex.equals("1")) || !(state.equals("0") || state.equals("1"))) //性别或者状态错误
		{
			msg = "4";
			mv.addObject("msg", msg);
			return mv;
		}
		if(state.equals("0") && !inductionTime.isEmpty()) //如果是在职
		{
			try
			{
				indTime = DateTimeConverter.dateTimeStrToLong(inductionTime, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				indTime = 0;
			}
		}
		else //离职
		{
			if(!inductionTime.isEmpty())
			{
				try
				{
					indTime = DateTimeConverter.dateTimeStrToLong(inductionTime, "yyyy-MM-dd HH:mm:ss");
				}
				catch(Exception e)
				{
					indTime = 0;
				}
			}
			if(!departureTime.isEmpty())
			{
				try
				{
					depTime = DateTimeConverter.dateTimeStrToLong(departureTime, "yyyy-MM-dd HH:mm:ss");
				}
				catch(Exception e)
				{
					depTime = 0;
				}
			}
		}
		Employee emp = new Employee(empId,name,Integer.valueOf(sex),phone,Integer.valueOf(state),indTime,depTime);
		int addResult = empSrv.addEmp(emp);
		if(addResult == 0) //成功
		{
			msg = "-1";
			mv.addObject("empId",empId);
			mv.setViewName("redirect:EmployeeModify.do");
		}
		else if(addResult == -1) //工号存在
		{
			msg = "2";
		}
		else
		{
			msg = "4";
		}
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改员工 页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = (String)request.getParameter("empId");
		String msg = (String)request.getParameter("msg");
		if(empId == null || empId.isEmpty()) //员工ID空
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		Employee modifyEmp = empSrv.getById(empId);
		if(modifyEmp == null) //员工ID不正确
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		String indTimeStr = "";
		String depTimeStr = "";
		String createTimeStr = "";
		if(modifyEmp.getCreateTime() >0 )
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyEmp.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(modifyEmp.getInductionTime() > 0)
		{
			indTimeStr = DateTimeConverter.dateTimeLongToStr(modifyEmp.getInductionTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if(modifyEmp.getDepartureTime() > 0)
		{
			depTimeStr = DateTimeConverter.dateTimeLongToStr(modifyEmp.getDepartureTime(), "yyyy-MM-dd HH:mm:ss");
		}

		mv.addObject("modifyEmp", modifyEmp);
		mv.addObject("indTimeStr", indTimeStr);
		mv.addObject("depTimeStr", depTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("EmployeeModify");
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:EmployeeModify.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String msg = "0";
		String empId = (String)request.getParameter("empId");
		String name = (String)request.getParameter("name");
		String sex = (String)request.getParameter("sex");
		String phone = (String)request.getParameter("phone");
		String state = (String)request.getParameter("state");
		String inductionTime = (String)request.getParameter("inductionTime");
		String departureTime = (String)request.getParameter("departureTime");
		long indTime = 0;
		long depTime = 0;
		if(empId == null || name == null || sex == null || phone == null || state == null || inductionTime == null || departureTime == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty()) //工号为空
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		Employee modifyEmp = empSrv.getById(empId);
		if(modifyEmp == null) //工号错误
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(name.isEmpty()) //姓名为空
		{
			msg = "2";
			mv.addObject("empId",empId);
			mv.addObject("msg", msg);
			return mv;
		}
		if(!(sex.equals("0") || sex.equals("1")) || !(state.equals("0") || state.equals("1"))) //性别或者状态错误
		{
			msg = "1";
			mv.addObject("empId",empId);
			mv.addObject("msg", msg);
			return mv;
		}
		if(state.equals("0") && !inductionTime.isEmpty()) //如果是在职
		{
			try
			{
				indTime = DateTimeConverter.dateTimeStrToLong(inductionTime, "yyyy-MM-dd HH:mm:ss");
			}
			catch(Exception e)
			{
				indTime = 0;
			}
		}
		else //离职
		{
			if(!inductionTime.isEmpty())
			{
				try
				{
					indTime = DateTimeConverter.dateTimeStrToLong(inductionTime, "yyyy-MM-dd HH:mm:ss");
				}
				catch(Exception e)
				{
					indTime = 0;
				}
			}
			if(!departureTime.isEmpty())
			{
				try
				{
					depTime = DateTimeConverter.dateTimeStrToLong(departureTime, "yyyy-MM-dd HH:mm:ss");
				}
				catch(Exception e)
				{
					depTime = 0;
				}
			}
		}
		modifyEmp.setName(name);
		modifyEmp.setSex(Integer.valueOf(sex));
		modifyEmp.setPhone(phone);
		modifyEmp.setState(Integer.valueOf(state));
		modifyEmp.setInductionTime(indTime);
		modifyEmp.setDepartureTime(depTime);
		if(empSrv.modifyEmp(modifyEmp) == 0) //修改成功
		{
			msg = "0";
		}
		else //修改失败
		{
			msg = "1";
		}
		mv.addObject("empId",empId);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeDel.do" }, method = RequestMethod.GET)
	public ModelAndView delEmployee(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:Employee.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		
		String empId = request.getParameter("empId");
		String msg;
		if(empId == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty() || empSrv.getById(empId) == null) //用户名为空或者用户不存在
		{
			msg = "1";
			mv.addObject("msg");
			return mv;
		}
		
		if(empSrv.delEmp(empId) == 0) //删除成功
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
	
	@RequestMapping(value = { "/EmployeeSelect.do" }, method = RequestMethod.GET)
	@ResponseBody
	public String selApartment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓选择搜索
	{
		request.setCharacterEncoding("UTF-8");
		String resultJson = "";
		String kw = request.getParameter("kw");
		String aptId = request.getParameter("aptId");
		String topParamStr = request.getParameter("topParam"); //返回前多少条
		if(kw == null || topParamStr == null) //非法请求
		{
			return resultJson;
		}
		int topParam = Integer.valueOf(topParamStr);
		if(loginCheck(session) && kw != null)
		{
			List<Employee> empList = new ArrayList<Employee>();
			if(aptId == null) //搜索所有员工
			{
				if(topParam == -1)
				{
					empList = empSrv.searchByIdOrName(kw, 1, 0); //返回所有记录
				}
				else if(topParam > 0)
				{
					empList = empSrv.searchByIdOrName(kw, 1, topParam); //返回n记录
				}
			}
			else //搜索公寓的员工
			{
				if(!aptId.isEmpty())
				{
					empList = empSrv.searchByIdOrNameAptId(aptId, kw);
				}
			}
			LinkedHashMap<String,List<Employee>> resultMap = new LinkedHashMap<String,List<Employee>>();
			resultMap.put("value", empList);
			ObjectMapper objMapper = new ObjectMapper();
			resultJson = objMapper.writeValueAsString(resultMap);
		}
		return resultJson;
	}
	
	@RequestMapping(value = { "/ApartmentEmp.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAptEmp(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmp");
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
		if(apt == null)
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
	
	@RequestMapping(value = { "/ApartmentEmp.do" }, method = RequestMethod.POST)
	public ModelAndView listAptEmp(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmp");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String state = request.getParameter("state");
		if(aptId == null || state == null) //非法请求
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
		if(!state.equals("0") && !state.equals("1") && !state.equals("2")) //参数错误
		{
			mv.addObject("aptId",aptId);
			mv.setViewName("redirect:ApartmentEmp.do");
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
		long resultCount = aptSrv.getAptEmpByAptSize(aptId, Integer.valueOf(state)); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<ApartmentEmp> aptEmps = aptSrv.getAptEmpByApt(aptId, Integer.valueOf(state), page, pageSize); //搜索记录
		mv.addObject("apt",apt);
		mv.addObject("state",state);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("aptEmps",aptEmps);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAptEmpAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmpAdd");
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
		mv.addObject("apt",apt);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addAptEmp(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("ApartmentEmpAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String empId = request.getParameter("empId");
		String msg = "";
		if(aptId == null || empId == null)
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
		if(empId.isEmpty())
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		Employee emp = empSrv.getById(empId);
		if(emp == null) //团贡不存在
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpByAptEmp(aptId, empId);
		if(aptEmp != null) //已存在
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.addObject("apt",apt);
			return mv;
		}
		ApartmentEmp newAptEmp = new ApartmentEmp(emp,apt);
		if(!aptSrv.addAptEmp(newAptEmp)) //添加失败
		{
			msg = "4";
			mv.addObject("apt",apt);
		}
		else //成功
		{
			msg = "-1";
			mv.addObject("aptId",apt.getId());
			mv.setViewName("redirect:ApartmentEmp.do");
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/ApartmentEmpDel.do" }, method = RequestMethod.GET)
	public ModelAndView delAptEmp(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //公寓员工删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:ApartmentEmp.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = "";
		if(aptId == null || aptEmpId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(aptId.isEmpty() || aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Apartment.do");
			return mv;
		}
		if(aptSrv.delAptEmp(aptEmpId) != 0) //删除失败
		{
			msg = "1";
		}
		else
		{
			msg = "0";
		}
		mv.addObject("aptId",aptId);
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeApt.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmpApt(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeApt");
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
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		Employee emp = empSrv.getById(empId);
		if(emp == null)
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
	
	@RequestMapping(value = { "/EmployeeApt.do" }, method = RequestMethod.POST)
	public ModelAndView listEmpApt(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeApt");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
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
		if(emp == null)
		{
			mv.setViewName("redirect:Employee.do");
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
		long resultCount = aptSrv.getAptEmpByEmpSize(empId); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<ApartmentEmp> aptEmps = aptSrv.getAptEmpByEmp(empId, page, pageSize); //搜索记录
		mv.addObject("emp",emp);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("aptEmps",aptEmps);
		mv.addObject("pageLength",pageLength);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectEmpAptAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeAptAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		if(empId == null)
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
		if(emp == null)
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		mv.addObject("emp",emp);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addEmpApt(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓添加
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("EmployeeAptAdd");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String aptId = request.getParameter("aptId");
		String empId = request.getParameter("empId");
		String msg = "";
		if(aptId == null || empId == null)
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
		if(emp == null)
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(aptId.isEmpty())
		{
			msg = "1";
			mv.addObject("msg",msg);
			mv.addObject("emp",emp);
			return mv;
		}
		Apartment apt = aptSrv.getApartment(aptId);
		if(apt == null) //公寓不存在
		{
			msg = "2";
			mv.addObject("msg",msg);
			mv.addObject("emp",emp);
			return mv;
		}
		ApartmentEmp aptEmp = aptSrv.getAptEmpByAptEmp(aptId, empId);
		if(aptEmp != null) //已存在
		{
			msg = "3";
			mv.addObject("msg",msg);
			mv.addObject("emp",emp);
			return mv;
		}
		ApartmentEmp newAptEmp = new ApartmentEmp(emp,apt);
		if(!aptSrv.addAptEmp(newAptEmp)) //添加失败
		{
			msg = "4";
			mv.addObject("emp",emp);
		}
		else //成功
		{
			msg = "-1";
			mv.addObject("empId",emp.getEmpId());
			mv.setViewName("redirect:EmployeeApt.do");
		}
		mv.addObject("msg",msg);
		return mv;
	}
	
	@RequestMapping(value = { "/EmployeeAptDel.do" }, method = RequestMethod.GET)
	public ModelAndView delEmpApt(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //员工公寓删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:EmployeeApt.do");
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		String empId = request.getParameter("empId");
		String aptEmpId = request.getParameter("aptEmpId");
		String msg = "";
		if(empId == null || aptEmpId == null)
		{
			mv.setViewName("404");
			return mv;
		}
		if(empId.isEmpty() || aptEmpId.isEmpty())
		{
			mv.setViewName("redirect:Employee.do");
			return mv;
		}
		if(aptSrv.delAptEmp(aptEmpId) != 0) //删除失败
		{
			msg = "1";
		}
		else
		{
			msg = "0";
		}
		mv.addObject("empId",empId);
		mv.addObject("msg",msg);
		return mv;
	}
}
