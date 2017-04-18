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
							房间ID
						</th>
						<th>
							<c:out value="${stuRoom.room.id}" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							房间名称
						</td>
						<td>
							<c:out value="${stuRoom.room.name}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							所属公寓
						</td>
						<td>
							<c:out value="${stuRoom.room.apartment.name}" />
						</td>
					</tr>	
					<tr class="success">
						<td>
							总床位数
						</td>
						<td>
							<c:out value="${stuRoom.room.capacity}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							空余床位数
						</td>
						<td>
							<c:out value="${stuRoom.room.capacity - stuRoom.room.currentAmout}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							住宿价格
						</td>
						<td>
							<c:out value="${stuRoom.room.price}" />
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
			<form class="form-horizontal" role="form" action="RoomDetailModify.do" onsubmit="return checkRoomDetailModify(this)" method="post">
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayId">记录ID</label>
					<div class="col-sm-7">
						<input class="form-control" id="detailIdDisplay" type="text" value="${stuRoom.id}" disabled/>
						<input class="form-control" id="detailId" name="detailId" type="hidden" value="${stuRoom.id}" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayStuId">学生学号</label>
					<div class="col-sm-7">
						<input class="form-control" id="stuIdDisplay" type="text" value="${stuRoom.student.stuId}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayName">学生姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="nameDisplay" type="text" value="${stuRoom.student.name}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displaySex">学生性别</label>
					<div class="col-sm-7">
					<c:if test="${stuRoom.student.sex == 0}" >
						<input class="form-control" id="sexDisplay" type="text" value="男" disabled/>
					</c:if>
					<c:if test="${stuRoom.student.sex == 1}" >
						<input class="form-control" id="sexDisplay" type="text" value="女" disabled/>
					</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayPhone">联系电话</label>
					<div class="col-sm-7">
						<input class="form-control" id="phoneDisplay" type="text" value="${stuRoom.student.phone}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputState">住宿状态</label>
					<div class="col-sm-7">
						<select class="form-control" id="state" name="state" onchange="roomDetailStateChange('state','endTimeCollapse')">
							<option value="0" <c:if test="${stuRoom.state == '0'}"> selected="selected" </c:if>>正在住宿</option>
							<option value="1" <c:if test="${stuRoom.state == '1'}"> selected="selected" </c:if>>已退住宿</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="inputStartTime">入住时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="startTimePicker" name="startTime" value="${startTimeStr}" readonly />
					</div>
				</div>
				<c:if test="${stuRoom.state == '0'}" >
				<div id="endTimeCollapse" class="collapse">
					<div class="form-group">
					 	<label class="col-sm-2 control-label" for="inputEndTime">退住时间</label>
						<div class="col-sm-7">
							<input class="form-control" id="endTimePicker" name="endTime" value="${endTimeStr}" readonly/>
						</div>
					</div>
				</div>
				</c:if>
				<c:if test="${stuRoom.state == '1'}" >
				<div id="endTimeCollapse" class="collapse in">
					<div class="form-group">
					 	<label class="col-sm-2 control-label" for="inputEndTime">退住时间</label>
						<div class="col-sm-7">
							<input class="form-control" id="endTimePicker" name="endTime" value="${endTimeStr}" readonly/>
						</div>
					</div>
				</div>
				</c:if>
				<div class="form-group">
					 	<label class="col-sm-2 control-label" for="displayCreateTime">创建时间</label>
						<div class="col-sm-7">
							<input class="form-control" id="createTimeDisplay" value="${createTimeStr}" disabled/>
						</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">修改住宿</button>
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
        		<strong>房间人数已满</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
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