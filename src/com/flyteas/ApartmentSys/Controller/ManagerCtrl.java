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

import com.flyteas.ApartmentSys.Domain.Manager;
import com.flyteas.ApartmentSys.Service.ManagerSrv;
import com.flyteas.ApartmentSys.Util.DateTimeConverter;
import com.flyteas.ApartmentSys.Util.MD5Encryptor;

/* 管理员控制器 */
@Controller
public class ManagerCtrl 
{
	@Autowired
	private ManagerSrv managerSrv;
	
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
	
	private boolean userRoleCheck(HttpSession session,int role) //检查当前用户是否为特定权限
	{
		Manager manager = (Manager)session.getAttribute("user");
		if(manager == null) //未登陆
		{
			return false;
		}
		if(manager.getRole() != role) //权限不一致
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
				lastLoginDateStr = DateTimeConverter.dateTimeLongToStr(user.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
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
			mv.setViewName("redirect:Login.do");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/404" }, method = RequestMethod.GET) //404
	public ModelAndView redirect404(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //404跳转
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("404");
		return mv;
	}
	
	@RequestMapping(value = { "/Login.do" }, method = RequestMethod.GET) //管理员登陆 页面
	public ModelAndView redirectLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(loginCheck(session)) //如果已经登陆，则跳转到主页面
		{
			mv.setViewName("redirect:index.jsp");
		}
		else
		{
			mv.setViewName("Login"); //跳转到登录页
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
		if(username == null || username.isEmpty()) //用户名为空
		{
			loginError = 2;
			mv.addObject("loginError",loginError);
			mv.setViewName("Login");
			return mv;
		}
		if(password == null || password.isEmpty()) //密码为空
		{
			loginError = 3;
			mv.addObject("loginError",loginError);
			mv.setViewName("Login");
			return mv;
		}
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
	
	@RequestMapping(value = { "/UserInfo.do" }, method = RequestMethod.GET)
	public ModelAndView getUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception // 用户信息页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		Manager user = (Manager)session.getAttribute("user");
		String createTimeStr = DateTimeConverter.dateTimeLongToStr(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"); //创建时间转换成字符串
		mv.addObject("createTimeStr", createTimeStr);
		mv.setViewName("UserInfo");
		return mv;
	}
	
	@RequestMapping(value = { "/UserInfo.do" }, method = RequestMethod.POST)
	public ModelAndView modifyUserInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception // 修改用户信息页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		Manager user = (Manager)session.getAttribute("user");
		String msg;
		String realName = (String)request.getParameter("realName");
		if(realName != null)
		{
			user.setRealName(realName);
		}
		String sex = (String)request.getParameter("sex");
		if(sex != null)
		{
			user.setSex(Integer.valueOf(sex));
		}
		String phone = (String)request.getParameter("phone");
		if(phone != null)
		{
			user.setPhone(phone);
		}
		if(managerSrv.modifyProfile(user))
		{
			session.removeAttribute("user");
			session.setAttribute("user", user);
			msg = "0"; //修改成功
		}
		else
		{
			msg = "1"; //修改失败
		}
		mv.addObject("msg",msg);
		String createTimeStr = DateTimeConverter.dateTimeLongToStr(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss"); //创建时间转换成字符串
		mv.addObject("createTimeStr", createTimeStr);
		mv.setViewName("UserInfo");
		return mv;
	}
	
	@RequestMapping(value = { "/PwdModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectUserPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改密码页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		mv.setViewName("PwdModify");
		return mv;
	}
	
	@RequestMapping(value = { "/PwdModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyUserPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改密码页面
	{
		request.setCharacterEncoding("UTF-8");
		Manager user;
		String oldPwd;
		String newPwd;
		String msg;
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		user = (Manager)session.getAttribute("user");
		if(request.getParameter("oldPwd") != null)
		{
			oldPwd = (String)request.getParameter("oldPwd");
		}
		else
		{
			msg = "1"; //原密码为空，也就是密码错误
			mv.addObject("msg",msg);
			mv.setViewName("PwdModify");
			return mv;
		}
		if(request.getParameter("newPwd") != null)
		{
			newPwd = (String)request.getParameter("newPwd");
		}
		else
		{
			msg = "2"; //新密码为空
			mv.addObject("msg",msg);
			mv.setViewName("PwdModify");
			return mv;
		}
		int modifyResult = managerSrv.modifyPwd(user, oldPwd, newPwd); //修改密码
		if(modifyResult == 0) //修改成功
		{
			msg = "0"; //修改成功
			session.setAttribute("user", user);
		}
		else if(modifyResult == -1) //原密码错误
		{
			msg = "1";
		}
		else
		{
			msg = "3";
		}
		mv.addObject("msg",msg);
		mv.setViewName("PwdModify");
		return mv;
	}
	
	@RequestMapping(value = { "/Manager.do" }, method = RequestMethod.GET)
	public ModelAndView redirectManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员管理页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		String msg = (String)request.getParameter("msg");
		if(msg != null) //传递消息
		{
			mv.addObject("msg",msg);
		}
		mv.setViewName("Manager");
		return mv;
	}
	
	@RequestMapping(value = { "/Manager.do" }, method = RequestMethod.POST)
	public ModelAndView searchManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //搜索管理员管理
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv;
		mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		String unameOrName = (String)request.getParameter("unameOrName");
		if(unameOrName == null) //请求错误
		{
			mv.setViewName("Manager");
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
		long resultCount = managerSrv.findByUnameRnameSize(unameOrName); //获取查询记录数
		
		
		long pageCount = resultCount/pageSize;
		if(resultCount%pageSize != 0) //如果不整除，则多一页
		{
			pageCount++;
		}
		if(resultCount == 0)
		{
			page = 0;
		}
		List<Manager> managers = managerSrv.findByUnameRname(unameOrName,page,pageSize);
		mv.addObject("unameOrName",unameOrName);
		mv.addObject("page",page);
		mv.addObject("pageSize",pageSize);
		mv.addObject("resultCount",resultCount);
		mv.addObject("pageCount",pageCount);
		mv.addObject("managers",managers);
		mv.addObject("pageLength",pageLength);
		mv.setViewName("Manager");
		return mv;
	}
	
	@RequestMapping(value = { "/ManagerAdd.do" }, method = RequestMethod.GET)
	public ModelAndView redirectAddManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		mv.setViewName("ManagerAdd");
		return mv;
	}
	
	@RequestMapping(value = { "/ManagerAdd.do" }, method = RequestMethod.POST)
	public ModelAndView addManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员添加页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		String msg;
		String username = (String)request.getParameter("username");
		String realName = (String)request.getParameter("realName");
		String sex = (String)request.getParameter("sex");
		String role = (String)request.getParameter("role");
		String phone = (String)request.getParameter("phone");
		String password = (String)request.getParameter("password");
		if(username == null || username.isEmpty()) //用户名为空
		{
			msg = "1"; //用户名不能为空
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
			return mv;
		}
		if(managerSrv.checkUsernameUsed(username)) //用户名已被使用
		{
			msg = "2"; //用户名已被使用
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
			return mv;
		}
		if(password == null || password.isEmpty()) //密码为空
		{
			msg = "3"; //密码为空
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
			return mv;
		}
		if(role == null || !(role.equals("0") || role.equals("1")))
		{
			msg = "4"; //角色错误，添加失败
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
			return mv;
		}
		if(sex == null || !(sex.equals("0") || sex.equals("1")))
		{
			msg = "4"; //性别错误，添加失败
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
			return mv;
		}
		if(managerSrv.add(username,password,Integer.valueOf(role),realName,Integer.valueOf(sex),phone)) //添加成功 跳转到修改页面
		{
			msg = "-1"; //添加成功
			mv.addObject("username",username);
			mv.addObject("msg",msg);
			mv.setViewName("redirect:ManagerModify.do");
		}
		else //添加失败
		{
			msg = "4"; //添加失败
			mv.addObject("msg",msg);
			mv.setViewName("ManagerAdd");
		}
		return mv;
	}
	
	@RequestMapping(value = { "/ManagerModify.do" }, method = RequestMethod.GET)
	public ModelAndView redirectModifyManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //修改管理员 页面
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		String username = (String)request.getParameter("username");
		String msg = (String)request.getParameter("msg");
		if(username == null || username.isEmpty()) //用户名空
		{
			mv.setViewName("redirect:Manager.do");
			return mv;
		}
		Manager modifyManager = managerSrv.getByUsername(username);
		if(modifyManager == null) //用户名不正确
		{
			mv.setViewName("redirect:Manager.do");
			return mv;
		}
		if(msg != null && !msg.isEmpty())
		{
			mv.addObject("msg",msg);
		}
		String lastLoginTimeStr = "";
		if(modifyManager.getLastLoginTime() >0 )
		{
			lastLoginTimeStr = DateTimeConverter.dateTimeLongToStr(modifyManager.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss");
		}
		String createTimeStr = "";
		if(modifyManager.getCreateTime() >0 )
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyManager.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		mv.addObject("modifyManager",modifyManager);
		mv.addObject("lastLoginTimeStr",lastLoginTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.setViewName("ManagerModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ManagerModify.do" }, method = RequestMethod.POST)
	public ModelAndView modifyManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员修改
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		String username = (String)request.getParameter("username");
		String realName = (String)request.getParameter("realName");
		String sex = (String)request.getParameter("sex");
		String role = (String)request.getParameter("role");
		String phone = (String)request.getParameter("phone");
		String password = (String)request.getParameter("password");
		String msg;
		if(username == null || realName == null || sex == null || role == null || phone == null || password == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(username.isEmpty()) //用户名空
		{
			mv.setViewName("redirect:Manager.do");
			return mv;
		}
		Manager modifyManager = managerSrv.getByUsername(username);
		if(modifyManager == null) //用户不存在
		{
			mv.setViewName("redirect:Manager.do");
			return mv;
		}
		modifyManager.setRealName(realName);
		modifyManager.setPhone(phone);
		if(!password.isEmpty()) //密码不为空，则修改密码
		{
			modifyManager.setPassword(MD5Encryptor.md5Encrypt(password));
		}
		if((role.equals("0") || role.equals("1"))) //如果角色信息合法
		{
			modifyManager.setRole(Integer.valueOf(role)); //更新角色信息
		}
		if((sex.equals("0") || sex.equals("1"))) //如果性别信息合法
		{
			modifyManager.setSex(Integer.valueOf(sex)); //更新性别信息
		}
		if(managerSrv.modifyProfile(modifyManager)) //修改成功
		{
			msg = "0";
		}
		else //修改失败
		{
			msg = "1";
		}
		String lastLoginTimeStr = "";
		if(modifyManager.getLastLoginTime() >0 ) //如果存在登陆时间信息，则显示
		{
			lastLoginTimeStr = DateTimeConverter.dateTimeLongToStr(modifyManager.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"); //转成字符串格式
		}
		String createTimeStr = "";
		if(modifyManager.getCreateTime() >0 ) //如果存在创建时间信息，则显示
		{
			createTimeStr = DateTimeConverter.dateTimeLongToStr(modifyManager.getCreateTime(), "yyyy-MM-dd HH:mm:ss"); //转成字符串格式
		}
		mv.addObject("modifyManager",modifyManager);
		mv.addObject("lastLoginTimeStr",lastLoginTimeStr);
		mv.addObject("createTimeStr",createTimeStr);
		mv.addObject("msg",msg);
		mv.setViewName("ManagerModify");
		return mv;
	}
	
	@RequestMapping(value = { "/ManagerDel.do" }, method = RequestMethod.GET)
	public ModelAndView delManager(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception //管理员删除
	{
		request.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		if(!loginCheck(session))
		{
			mv.setViewName("redirect:index.jsp");
			return mv;
		}
		if(!userRoleCheck(session,0)) //检查是否是系统管理员权限
		{
			mv.setViewName("404"); //权限不是系统管理员
			return mv;
		}
		
		String username = request.getParameter("username");
		String msg;
		if(username == null) //非法请求
		{
			mv.setViewName("404");
			return mv;
		}
		if(username.isEmpty() || managerSrv.getByUsername(username) == null) //用户名为空或者用户不存在
		{
			msg = "1";
			mv.addObject("msg");
			mv.setViewName("redirect:Manager.do");
			return mv;
		}
		
		Manager user = (Manager)session.getAttribute("user");
		if(user.getUsername().equals(username)) //如果删除的是当前登陆的账号
		{
			if(managerSrv.delByUsername(username)) //删除成功
			{
				msg = "-1"; //删除自己成功
				mv.setViewName("redirect:Logout.do"); //注销登陆
				return mv;
			}
			else
			{
				msg = "1";
			}
		}
		else
		{
			if(managerSrv.delByUsername(username)) //删除成功
			{
				msg = "0";
			}
			else
			{
				msg = "1";
			}
		}
		mv.addObject("msg",msg);
		mv.setViewName("redirect:Manager.do");
		return mv;
	}
	
}
