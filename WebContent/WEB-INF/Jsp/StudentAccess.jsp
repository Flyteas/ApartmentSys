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
   <script src="resources/js/bootstrap-suggest.min.js"></script>
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
						<li class="active">
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
	<br><br><br><br><br><br><br>
	<div class="row clearfix">
		<div class="col-md-3 column">
		</div>
		<div class="col-md-6 column">
			<form class="form-horizontal" role="form" id="stuAccForm" action="StudentAccess.do" method="post">
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputStuId">学号</label>
					<div class="col-sm-6">
						<input class="form-control" id="stuId" name="stuId" type="text" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputApt">选择公寓</label>
					<div class="col-sm-6">
						<div class="input-group">
                        	<input type="text" class="form-control" id="aptSelect" autocomplete="off">
                        	<div class="input-group-btn">
                            	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="">
                                	<span class="caret"></span>
                            	</button>
                            	<ul class="dropdown-menu dropdown-menu-right" role="menu"></ul>
                        	</div>
                    	</div>
						<input class="form-control" id="aptId" name="aptId" type="hidden" value="" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" for="inputStuId">进出类型</label>
					<div class="col-sm-6">
						<select class="form-control" id="type" name="type" >
							<option selected="selected" value="0">进</option>
							<option value="1">出</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-10">
						 <button class="btn btn-success btn-lg" type="submit">添加记录</button>
					</div>
				</div>
			</form>
			<c:if test="${msg == '0'}">
      		<div class="alert alert-success col-sm-10" role="alert" id="delSuccessAlert">
        		<strong>添加成功</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '1'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>请输入学号</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '2'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>学生不存在</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '3'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>请选择公寓</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '4'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>公寓不存在</strong>
    		</div>
    		</c:if>
    		<c:if test="${msg == '5'}">
      		<div class="alert alert-danger col-sm-10" role="alert" id="delFailAlert">
        		<strong>添加失败</strong>
    		</div>
    		</c:if>
		</div>
	</div>
</div>
<script src="resources/js/bootstrap-suggest-init-EmpApt.js"></script>
</body>
</html>