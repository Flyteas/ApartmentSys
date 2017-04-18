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
						<li>
							 <a href="Apartment.do">公寓管理</a>
						</li>
						<li>
							 <a href="Employee.do">员工管理</a>
						</li>
						<li class="active">
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
							学生学号
						</th>
						<th>
							<c:out value="${stu.stuId}" />
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="success">
						<td>
							学生姓名
						</td>
						<td>
							<c:out value="${stu.name}" />
						</td>
					</tr>
					<tr class="success">
						<td>
							学生性别
						</td>
						<td>
							<c:if test="${stu.sex == 0}">男</c:if>
							<c:if test="${stu.sex == 1}">女</c:if>
						</td>
					</tr>	
					<tr class="success">
						<td>
							联系电话
						</td>
						<td>
							<c:out value="${stu.phone}" />
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
			<form class="form-horizontal" role="form" id="stuRoomForm" action="StudentRoom.do" method="post">
				<div class="form-group">
					 <label class="col-sm-3 control-label" for="inputState">住宿状态</label>
					<div class="col-sm-6">
						<select class="form-control" id="state" name="state" >
							<option value="0">正在住宿</option>
							<option value="1">已退住宿</option>
							<option value="2" selected="selected">所有状态</option>
						</select>
						<input class="form-control" id="stuId" name="stuId" type="hidden" value="${stu.stuId}"/>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">列出记录</button>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="button" onclick="window.open('StudentRoomAdd.do?stuId=${stu.stuId}')">添加住宿</button>
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
			<label for="TableName">记录列表</label><br>
			<c:if test="${page != null}" >
			<label for="TableCount">记录数: <c:out value="${resultCount}" />&nbsp;&nbsp;&nbsp;<c:out value="${page}" />/<c:out value="${pageCount}" />页</label><br>
			</c:if>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							记录ID
						</th>
						<th>
							公寓ID
						</th>
						<th>
							公寓名
						</th>
						<th>
							房间ID
						</th>
						<th>
							房间名
						</th>
						<th>
							住宿状态
						</th>
						<th>
							入住时间
						</th>
						<th>
							退住时间
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="stuRoom" items="${stuRooms}">
					<tr class="success">
						<td>
							<c:out value="${stuRoom.id}"/>
						</td>
						<td>
							<c:out value="${stuRoom.room.apartment.id}"/>
						</td>
						<td>
							<c:out value="${stuRoom.room.apartment.name}"/>
						</td>
						<td>
							<c:out value="${stuRoom.room.id}"/>
						</td>
						<td>
							<c:out value="${stuRoom.room.name}"/>
						</td>
						<td>
							<c:if test="${stuRoom.state == 0}">正在住宿</c:if>
							<c:if test="${stuRoom.state == 1}">已退住宿</c:if>
						</td>
						<td>
							<c:if test="${stuRoom.liveStartTime > 0}">
								<script>timestampToDateStr(<c:out value="${stuRoom.liveStartTime}" />)</script>
							</c:if>
						</td>
						<td>
							<c:if test="${stuRoom.liveEndTime > 0}">
								<script>timestampToDateStr(<c:out value="${stuRoom.liveEndTime}" />)</script>
							</c:if>
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="modifyStudentRoomSubmit('StudentRoomModify.do?stuId=${stu.stuId}&stuRoomId=${stuRoom.id}')">查看修改</button>
							<button type="button" class="btn btn-success btn-xs" onclick="delStudentRoomSubmit('StudentRoomDel.do?stuId=${stu.stuId}&stuRoomId=${stuRoom.id}','${stuRoom.id}')">删除记录</button>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
			<c:if test="${page != null}" >
			<form class="form-horizontal" role="form" id="stuRoomPageForm" action="StudentRoom.do" method="post" >
				<div class="form-group">
					<div class="col-sm-7">
						<input class="form-control" id="statePage" name="state" type="hidden" value="<c:out value="${state}" />"/>
						<input class="form-control" id="stuIdPage" name="stuId" type="hidden" value="${stu.stuId}"/>
						<input class="form-control" id="page" name="page" type="hidden" />
					</div>
				</div>
			</form>
			<div class="col-md-offset-9 col-md-4">
				<ul class="pagination">
					<fmt:parseNumber var="pageTmp" integerOnly="true" value="${(page-0.5)/pageLength}" />
					<c:set var="pageStart" value="${pageTmp * pageLength + 1}" />
					<c:if test="${page > pageLength}">
						<li><a href="javascript:changePage('stuRoomPageForm','page',<c:out value="${pageStart-1}"/>)">&laquo;</a></li>
					</c:if>
					<c:if test="${(pageStart + pageLength - 1) > pageCount}">
						<c:set var="pageEnd" value="${pageCount}" />
					</c:if>
					<c:if test="${(pageStart + pageLength - 1) <= pageCount}">
						<c:set var="pageEnd" value="${pageStart + pageLength - 1}" />
					</c:if>
					<c:forEach var="linkPage" begin="${pageStart}" end="${pageEnd}" step="1">
						<c:if test="${linkPage == page}">
							<li class="active"><a href="javascript:changePage('stuRoomPageForm','page',<c:out value="${linkPage}"/>);"><c:out value="${linkPage}"/></a></li>
						</c:if>
						<c:if test="${linkPage != page}">
							<li><a href="javascript:changePage('stuRoomPageForm','page',<c:out value="${linkPage}"/>)"><c:out value="${linkPage}"/></a></li>
						</c:if>
					</c:forEach>
					<c:if test="${pageEnd < pageCount}">
						<li><a href="javascript:changePage('stuRoomPageForm','page',<c:out value="${pageStart + pageLength}"/>)">&raquo;</a></li>
					</c:if>
				</ul>
			</div>
			</c:if>
      		<c:if test="${stuRooms != null && fn:length(stuRooms) == 0}">
      		<div class="alert alert-danger" role="alert" id="noAptAlert">
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