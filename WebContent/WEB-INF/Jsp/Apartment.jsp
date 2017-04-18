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
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" id="apartmentForm" action="Apartment.do" method="post">
				<div class="form-group">
					 <label class="col-sm-3 control-label" for="inputNameOrAddr">公寓名或地址</label>
					<div class="col-sm-7">
						<input class="form-control" id="nameOrAddr" name="nameOrAddr" type="text" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">搜索公寓楼</button>
						 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						 <button class="btn btn-success btn-lg" type="button" onclick="window.open('ApartmentAdd.do')">添加公寓楼</button>
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
			<label for="TableName">公寓楼列表</label><br>
			<c:if test="${page != null}" >
			<label for="TableCount">记录数: <c:out value="${resultCount}" />&nbsp;&nbsp;&nbsp;<c:out value="${page}" />/<c:out value="${pageCount}" />页</label><br>
			</c:if>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>
							公寓楼ID
						</th>
						<th>
							公寓楼名
						</th>
						<th>
							公寓地址
						</th>
						<th>
							已用床位
						</th>
						<th>
							空余床位
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="apart" items="${aparts}">
					<tr class="success">
						<td>
							<c:out value="${apart.id}"/>
						</td>
						<td>
							<c:out value="${apart.name}"/>
						</td>
						<td>
							<c:out value="${apart.address}" />
						</td>
						<td>
							<c:out value="${apart.usedCapacity}" />
						</td>
						<td>
							<c:out value="${apart.capacity - apart.usedCapacity}" />
						</td>
						<td>
							<button type="button" class="btn btn-success btn-xs" onclick="modifyAptSubmit('ApartmentModify.do?aptId=${apart.id}')">查看修改</button>
							<button type="button" class="btn btn-success btn-xs" onclick="roomAptSubmit('ApartmentRoom.do?aptId=${apart.id}')">房间管理</button>
							<button type="button" class="btn btn-success btn-xs" onclick="empAptSubmit('ApartmentEmp.do?aptId=${apart.id}')">员工管理</button>
							<button type="button" class="btn btn-success btn-xs" onclick="rotaAptSubmit('ApartmentRota.do?aptId=${apart.id}')">值班管理</button>
							<button type="button" class="btn btn-success btn-xs" onclick="stuAccAptSubmit('ApartmentStuAcc.do?aptId=${apart.id}')">查寝管理</button>
							<button type="button" class="btn btn-success btn-xs" onclick="delAptSubmit('ApartmentDel.do?aptId=${apart.id}','${apart.name}')">删除公寓</button>
						</td>
					</tr>
					</c:forEach>	
				</tbody>
			</table>
			<c:if test="${page != null}" >
			<form class="form-horizontal" role="form" id="aptPageForm" action="Apartment.do" method="post" >
				<div class="form-group">
					<div class="col-sm-7">
						<input class="form-control" id="nameOrAddrPage" name="nameOrAddr" type="hidden" value="<c:out value="${nameOrAddr}" />"/>
						<input class="form-control" id="page" name="page" type="hidden" />
					</div>
				</div>
			</form>
			<div class="col-md-offset-9 col-md-4">
				<ul class="pagination">
					<fmt:parseNumber var="pageTmp" integerOnly="true" value="${(page-0.5)/pageLength}" />
					<c:set var="pageStart" value="${pageTmp * pageLength + 1}" />
					<c:if test="${page > pageLength}">
						<li><a href="javascript:changePage('aptPageForm','page',<c:out value="${pageStart-1}"/>)">&laquo;</a></li>
					</c:if>
					<c:if test="${(pageStart + pageLength - 1) > pageCount}">
						<c:set var="pageEnd" value="${pageCount}" />
					</c:if>
					<c:if test="${(pageStart + pageLength - 1) <= pageCount}">
						<c:set var="pageEnd" value="${pageStart + pageLength - 1}" />
					</c:if>
					<c:forEach var="linkPage" begin="${pageStart}" end="${pageEnd}" step="1">
						<c:if test="${linkPage == page}">
							<li class="active"><a href="javascript:changePage('aptPageForm','page',<c:out value="${linkPage}"/>);"><c:out value="${linkPage}"/></a></li>
						</c:if>
						<c:if test="${linkPage != page}">
							<li><a href="javascript:changePage('aptPageForm','page',<c:out value="${linkPage}"/>)"><c:out value="${linkPage}"/></a></li>
						</c:if>
					</c:forEach>
					<c:if test="${pageEnd < pageCount}">
						<li><a href="javascript:changePage('aptPageForm','page',<c:out value="${pageStart + pageLength}"/>)">&raquo;</a></li>
					</c:if>
				</ul>
			</div>
			</c:if>
      		<c:if test="${aparts != null && fn:length(aparts) == 0}">
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