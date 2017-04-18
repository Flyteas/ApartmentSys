<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
   <title>公寓管理系统</title>
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
   <link href="resources/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
   <script src="resources/js/jquery-1.12.2.min.js"></script>
   <script src="resources/js/bootstrap.min.js"></script>
   <script src="resources/js/bootstrap-suggest.min.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
   <script src="resources/js/bootstrap-datetimepicker/datetimepickerInit.js"></script>
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
						<li class="active">
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
							工号
						</th>
						<th>
							<c:out value="${emp.empId}" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							姓名
						</td>
						<td>
							<c:out value="${emp.name}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							性别
						</td>
						<td>
							<c:if test="${emp.sex == 0}">男</c:if>
							<c:if test="${emp.sex == 1}">女</c:if>
						</td>
					</tr>
					<tr class="success">
						<td>
							电话
						</td>
						<td>
							<c:out value="${emp.phone}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							状态
						</td>
						<td>
							<c:if test="${emp.state == 0}">在职</c:if>
							<c:if test="${emp.state == 1}">离职</c:if>
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
			<form class="form-horizontal" role="form" action="RotaAdd.do" onsubmit="return checkRotaAdd(this)" method="post">
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputApt">选择公寓</label>
					<div class="col-sm-7">
						<div class="input-group">
                        	<input type="text" class="form-control" id="aptSelect" autocomplete="off">
                        	<div class="input-group-btn">
                            	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="">
                                	<span class="caret"></span>
                            	</button>
                            	<ul class="dropdown-menu dropdown-menu-right" role="menu"></ul>
                        	</div>
                    	</div>
                    	<input class="form-control" id="aptId" name="aptId" type="hidden" value=""/>
                    	<input class="form-control" id="empId" name="empId" type="hidden" value="${emp.empId}" />
                    	<input class="form-control" id="type" name="type" type="hidden" value="1" />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputStartTime">开始时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="startTimePicker" name="startTime" readonly />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputEndTime">结束时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="endTimePicker" name="endTime" readonly />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">添加值班</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '1'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>请输入开始时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>请输入结束时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>开始时间不能大于结束时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '4'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>请选择公寓楼</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '5'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>员工不属于该公寓楼</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '6'}">
			<div class="alert alert-danger" role="alert" id="addResultAlert">
        		<strong>添加失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>
<script src="resources/js/bootstrap-suggest-init-EmpRota.js"></script>
</body>
</html>