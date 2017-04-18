<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
   <title>公寓管理系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
   <script src="resources/js/common.js"></script>
</head>
<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
				<div class="navbar-header">
					 <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">管理<span class="sr-only"></span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="index.jsp">主页</a>
				</div>
				
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li>
							 <a href="Apartment.do">公寓管理</a>
						</li>
						<li>
							 <a href="Employee.do">员工管理</a>
						</li>
						<li>
							 <a href="Student.do">学生管理</a>
						</li>
						<li>
							 <a href="VisitRecord.do">访客管理</a>
						</li>
						<li>
							 <a href="StudentAccess.do">门禁管理</a>
						</li>
						<c:if test="${user.role == 0}">
						<li class="active">
							 <a href="Manager.do">管理员管理</a>
						</li>
						</c:if>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
							 <a class="dropdown-toggle" href="#" data-toggle="dropdown">${user.realName}<strong class="caret"></strong></a>
							<ul class="dropdown-menu">
								<li>
									 <a href="UserInfo.do">个人资料</a>
								</li>
								<li>
									 <a href="PwdModify.do">修改密码</a>
								</li>
								<li class="divider">
								</li>
								<li>
									 <a href="Logout.do">注销</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</nav>
		</div>
	</div>
	<br><br><br><br>
	<div class="row clearfix">
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" action="ManagerModify.do" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="displayUsername">用户名</label>
					<div class="col-sm-7">
						<input class="form-control" id="username" name="username" type="text" value="${modifyManager.username}" disabled />
						<input class="hidden" id="username" name="username" type="text" value="${modifyManager.username}"/>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputRealName">真实姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="realName" name="realName" type="text" value="${modifyManager.realName}" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputSex">用户性别</label>
					<div class="col-sm-7">
						<select class="form-control" id="sex" name="sex" >
							<option value="0" <c:if test="${modifyManager.sex == '0'}"> selected="selected" </c:if>>男</option>
							<option value="1" <c:if test="${modifyManager.sex == '1'}"> selected="selected" </c:if>>女</option>
						</select>	
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputRole">用户角色</label>
					<div class="col-sm-7">
						<select class="form-control" id="role" name="role" >
							<option value="0" <c:if test="${modifyManager.role == '0'}"> selected="selected" </c:if>>系统管理员</option>
							<option value="1" <c:if test="${modifyManager.role == '1'}"> selected="selected" </c:if>>公寓管理员</option>
						</select>	
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPhone">联系电话</label>
					<div class="col-sm-7">
						<input class="form-control" id="phone" name="phone" value="${modifyManager.phone}" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPwd">修改密码</label>
					<div class="col-sm-7">
						<input class="form-control" id="password" name="password" type="text" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="displayLoginTime">登陆时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="lastLoginTime"  type="text" value="${lastLoginTimeStr}" disabled/>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="displayIP">登陆IP</label>
					<div class="col-sm-7">
						<input class="form-control" id="lastLoginIP" type="text" value="${modifyManager.lastLoginIP}" disabled/>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="displayCreateTime">创建时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="createTime" type="text" value="${createTimeStr}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">修改信息</button>
					</div>
				</div>
			</form>
    		<c:if test="${msg == '0'}">
			<div class="alert alert-success" role="alert" id="modifyResultAlert">
        		<strong>修改成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '1'}">
			<div class="alert alert-danger" role="alert" id="modifyResultAlert">
        		<strong>修改失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '-1'}">
			<div class="alert alert-success" role="alert" id="addResultAlert">
        		<strong>添加成功</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>

</body>
</html>