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
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" action="RotaModify.do" method="post">
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayRotaId">记录ID</label>
					<div class="col-sm-7">
						<input class="form-control" id="rotaId" type="text" value="${rota.id}" disabled />
						<input class="hidden" id="rotaId" name="rotaId" type="text" value="${rota.id}"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayEmpId">工号</label>
					<div class="col-sm-7">
						<input class="form-control" id="empId" type="text" value="${rota.apartmentEmp.employee.empId}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayEmpName">姓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="empName" type="text" value="${rota.apartmentEmp.employee.name}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displaySex">性别</label>
					<div class="col-sm-7">
						<c:if test="${rota.apartmentEmp.employee.sex == 0}">
						<input class="form-control" id="empSex" type="text" value="男" disabled/>
						</c:if>
						<c:if test="${rota.apartmentEmp.employee.sex == 1}">
						<input class="form-control" id="empSex" type="text" value="女" disabled/>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayPhone">联系电话</label>
					<div class="col-sm-7">
						<input class="form-control" id="phone" type="text" value="${rota.apartmentEmp.employee.phone}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayState">状态</label>
					<div class="col-sm-7">
						<c:if test="${rota.apartmentEmp.employee.state == 0}">
						<input class="form-control" id="empSex" type="text" value="在职" disabled/>
						</c:if>
						<c:if test="${rota.apartmentEmp.employee.state == 1}">
						<input class="form-control" id="empSex" type="text" value="离职" disabled/>
						</c:if>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayAptId">公寓ID</label>
					<div class="col-sm-7">
						<input class="form-control" id="aptId" type="text" value="${rota.apartmentEmp.apartment.id}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayAptName">公寓名</label>
					<div class="col-sm-7">
						<input class="form-control" id="aptName" type="text" value="${rota.apartmentEmp.apartment.name}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="displayAptAddr">公寓地址</label>
					<div class="col-sm-7">
						<input class="form-control" id="aptAddr" type="text" value="${rota.apartmentEmp.apartment.address}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputStartTime">开始时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="startTimePicker" name="startTime" value="${startTimeStr}" readonly />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputEndTime">结束时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="endTimePicker" name="endTime" value="${endTimeStr}" readonly />
					</div>
				</div>
				<div class="form-group">
					 <label class="col-sm-2 control-label" for="displayCreateTime">创建时间</label>
					<div class="col-sm-7">
						<input class="form-control" id="createTime" value="${createTimeStr}" disabled/>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">修改值班</button>
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
        		<strong>请输入开始时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
			<div class="alert alert-danger" role="alert" id="modifyResultAlert">
        		<strong>请输入结束时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
			<div class="alert alert-danger" role="alert" id="modifyResultAlert">
        		<strong>开始时间不能大于结束时间</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '4'}">
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