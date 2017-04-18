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
					<tr class="success">
						<td>
							总床位数
						</td>
						<td>
							<c:out value="${apt.capacity}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							空余床位数
						</td>
						<td>
							<c:out value="${apt.capacity- apt.usedCapacity}" />
						</td>
					</tr>	
				</tbody>
			</table>
		</div>
	</div>
	<br>
	<div class="row clearfix">
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" action="RoomAdd.do" onsubmit="return checkRoomAdd(this)" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputName">房间名称</label>
					<div class="col-sm-7">
						<input class="form-control" id="name" name="name" type="text" />
						<input class="form-control" id="aptId" name="aptId" type="hidden" value="${apt.id}" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputCap">人数容量</label>
					<div class="col-sm-7">
						<input class="form-control" id="capacity" name="capacity" type="number" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputPrice">住宿价格</label>
					<div class="col-sm-7">
						<input class="form-control" id="price" name="price" type="text" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">添加房间</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '1'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>房间名不能为空   添加失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>人数容量不能为空   添加失败</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>添加失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>

</body>
</html>