<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
									 <a href="PwdMidify.do">修改密码</a>
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
			<form class="form-horizontal" role="form" action="Manager.do" method="post">
				<div class="form-group">
					 <label class="col-sm-3 control-label" for="inputUnameOrName">用户名或姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="unameOrName" name="unameOrName" type="text" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">搜索管理员</button>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="button" onclick="window.open('ManagerAdd.do')">添加管理员</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '0'}">
      		<div class="alert alert-success col-sm-10" role="alert" id="delSuccessAlert">
        		<strong>删除成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '1'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>删除失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
	<br><br><br>
	<div class="row clearfix">
		<div class="col-md-1 column">
		</div>
		<div class="col-md-10 column">
			<label for="TableName">管理员列表</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							用户名
						</th>
						<th>
							真实姓名
						</th>
						<th>
							用户性别
						</th>
						<th>
							用户角色
						</th>
						<th>
							联系电话
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="manager" items="${managers}">
					<tr class="success">
						<td>
							<c:out value="${manager.username}"/>
						</td>
						<td>
							<c:out value="${manager.realName}"/>
						</td>
						<td>
							<c:if test="${manager.sex == '0'}">男</c:if>
							<c:if test="${manager.sex == '1'}">女</c:if>
						</td>
						<td>
							<c:if test="${manager.role == '0'}">系统管理员</c:if>
							<c:if test="${manager.role == '1'}">公寓管理员</c:if>
						</td>
						<td>
							<c:out value="${manager.phone}"/>
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="modifyManagerSubmit('ManagerModify.do?username=${manager.username}')">查看修改</button>
							<c:if test="${manager.username != user.username}">
							<button type="button" class="btn btn-success btn-xs" onclick="delManagerSubmit('ManagerDel.do?username=${manager.username}','${manager.realName}')">删除用户</button>
							</c:if>
							<c:if test="${manager.username == user.username}">
							<button type="button" class="btn btn-success btn-xs" onclick="delManagerSelf('ManagerDel.do?username=${manager.username}','${manager.realName}')">删除自己</button>
							</c:if>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
      		<c:if test="${managers != null && fn:length(managers) == 0}">
      		<div class="alert alert-danger" role="alert" id="noManagerAlert">
        		<strong>搜索结果为空</strong>
    		</div>
    		</c:if>
		</div>
		<div class="col-md-1 column">
		</div>
	</div>
</div>
</body>
</html>