<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
						<li class="active">
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
						<li>
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
		<div class="col-sm-offset-3 col-md-6 column">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							公寓ID
						</th>
						<th>
							<c:out value="${apt.id}" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							公寓名称
						</td>
						<td>
							<c:out value="${apt.name}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							公寓地址
						</td>
						<td>
							<c:out value="${apt.address}" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<br><br><br><br><br>
	<div class="row clearfix">
		<div class="col-md-1 column">
		</div>
		<div class="col-md-10 column">
			<label for="TableName">未归寝学生列表</label><br>
			<label for="TableCount">记录数: ${fn:length(noBackListMap)}</label><br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							学生学号
						</th>
						<th>
							学生姓名
						</th>
						<th>
							学生性别
						</th>
						<th>
							学生电话
						</th>
						<th>
							房间ID
						</th>
						<th>
							房间名
						</th>
						<th>
							离开公寓时间
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="noBackStuMap" items="${noBackListMap}">
					<c:set var="stuRoom" value="${noBackStuMap.key}" />
					<c:set var="lastLeaveTimeStr" value="${noBackStuMap.value}" />
					<tr class="success">
						<td>
							<c:out value="${stuRoom.student.stuId}"/>
						</td>
						<td>
							<c:out value="${stuRoom.student.name}"/>
						</td>
						<td>
							<c:if test="${stuRoom.student.sex == 0}">男</c:if>
							<c:if test="${stuRoom.student.sex == 1}">女</c:if>
						</td>
						<td>
							<c:out value="${stuRoom.student.phone}" />
						</td>
						<td>
							<c:out value="${stuRoom.room.id}" />
						</td>
						<td>
							<c:out value="${stuRoom.room.name}" />
						</td>
						<td>
							<c:out value="${lastLeaveTimeStr}" />
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
      		<c:if test="${noBackListMap != null && fn:length(noBackListMap) == 0}">
      		<div class="alert alert-danger" role="alert" id="noAptAlert">
        		<strong>结果为空</strong>
    		</div>
    		</c:if>
		</div>
		<div class="col-md-1 column">
		</div>
	</div>
</div>
</body>
</html>