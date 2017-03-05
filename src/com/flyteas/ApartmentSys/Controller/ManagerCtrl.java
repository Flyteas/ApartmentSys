package com.flyteas.ApartmentSys.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.flyteas.ApartmentSys.Domain.Manager;
import com.flyteas.ApartmentSys.Service.ManagerSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;

/* 管理员控制器 */
@Controller
public class ManagerCtrl 
{
	@Autowired
	private ManagerSrv managerSrv;
	
	private DateTimeConverter dateTimeConverter = new DateTimeConverter();
	
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
	
	@RequestMapping(value = { "/Index.do" }, method = RequestMethod.GET)
	public ModelAndView index(HttpSession session) throws Exception //首页转跳
	{
		Manager user;
		String lastLoginDateStr;
		ModelAndView mv;
		mv = new ModelAndView();
		user = (Manager)session.getAttribute("user");
		if(user != null) //已登陆
		{
			if(user.getLastLoginTime() >0 ) //等于0则是第一次登陆
			{
				lastLoginDateStr = dateTimeConverter.dateTimeLongToStr(user.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
			}
			else
			{
				lastLoginDateStr = "";
			}
			mv.addObject("lastLoginDateStr",lastLoginDateStr);
			mv.setViewName("Home");
		}
		else
		{
			mv.setViewName("Login");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/Login.do" }, method = RequestMethod.POST) //管理员登陆
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		Manager user;
		String username;
		String password;
		int loginError;
		ModelAndView mv;
		mv = new ModelAndView();
		if(loginCheck(session)) //已登陆
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		username = request.getParameter("username");
		password = request.getParameter("password");
		user = managerSrv.login(username, password, request.getRemoteAddr());
		if(user == null) //登陆失败
		{
			loginError = 1;
			mv.addObject("loginError",loginError);
			mv.setViewName("Login");
		}
		else
		{
			session.setAttribute("user",user);
			mv.setViewName("redirect:index.jsp");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/Logout.do" }, method = RequestMethod.GET) //用户注销
	public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		session.removeAttribute("user");
		response.sendRedirect("index.jsp");
	}
	
}
