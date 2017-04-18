/*
 * 常用JS方法
 */


function timestampToDateStr(timestamp)
{
	var date = new Date(timestamp);
	var dateStr = date.toLocaleString();
	document.write(dateStr);
	return dateStr;
}

function checkPwdModify(form)
{
	if(form.oldPwd.value == "" || form.oldPwd.value == null)
	{
		alert("原密码不能为空");
		return false;
	}
	if(form.newPwd.value == "" || form.newPwd.value == null)
	{
		alert("新密码不能为空");
		return false;
	}
	if(form.newPwdConfirm.value == "" || form.newPwdConfirm.value == null)
	{
		alert("确认密码不能为空");
		return false;
	}
	if(form.oldPwd.value == form.newPwd.value)
	{
		alert("新密码不能与原密码相同");
		return false;
	}
	if(form.newPwd.value != form.newPwdConfirm.value)
	{
		alert("两次密码输入不一致");
		return false;
	}
	return true;
}

function checkManagerAdd(form)
{
	if(form.username.value == null || form.username.value == "")
	{
		alert("用户名不能为空");
		return false;
	}
	if(form.realName.value == null || form.realName.value == "")
	{
		alert("真实姓名不能为空");
		return false;
	}
	if(form.password.value == null || form.password.value == "")
	{
		alert("登陆密码不能为空");
		return false;
	}
	return true;
}


function modifyManagerSubmit(modifyUrl)
{
	window.open(modifyUrl);
}

function delManagerSubmit(delUrl,realName)
{
	if(confirm('确定删除管理员 '+realName+' ?'))
	{
		location.href(delUrl);
	}
}

function delManagerSelf(delUrl,realName) //删除当前登陆账号
{
	if(confirm('确定删除当前登陆用户 '+realName+' ?\n删除成功后会注销当前登陆!'))
	{
		location.href(delUrl);
	}
}

function changePage(formId,pageInputId,page)
{
	document.getElementById(pageInputId).value = page;
	document.getElementById(formId).submit();
}

function modifyAptSubmit(modifyUrl)
{
	window.open(modifyUrl);
}

function roomAptSubmit(url)
{
	window.open(url);
}

function empAptSubmit(url)
{
	window.open(url);	
}

function rotaAptSubmit(url)
{
	window.open(url);
}

function stuAccAptSubmit(url)
{
	window.open(url);
}

function delAptSubmit(delUrl,aptName)
{
	if(confirm('确定删除公寓楼 '+aptName+' ?'))
	{
		location.href(delUrl);
	}
}

function checkAptAdd(form)
{
	if(form.name.value == null || form.name.value == "")
	{
		alert("公寓名不能为空");
		return false;
	}
	return true;
}

function aptEmpSubmit(url)
{
	window.open(url);
}

function rotaEmpSubmit(url)
{
	window.open(url);
}

function modifyEmpSubmit(url)
{
	window.open(url);
}

function delEmpSubmit(url,empName)
{
	if(confirm('确定删除员工 '+empName+' ?'))
	{
		location.href(url);
	}
}

function empStateChange(stateId,depInputId)
{
	var state = document.getElementById(stateId).value;
	var depInput = document.getElementById(depInputId);
	if(state == '0') //在职
	{
		depInput.setAttribute("class", "collapse"); //隐藏离职时间输入框
	}
	else
	{
		depInput.setAttribute("class", "collapse in"); //显示
	}
}

function checkEmpAdd(form)
{
	if(form.empId.value == null || form.empId.value == "")
	{
		alert("工号不能为空");
		return false;
	}
	if(form.name.value == null || form.name.value == "")
	{
		alert("姓名不能为空");
		return false;
	}
	return true;	
}

function roomStuSubmit(url)
{
	window.open(url);
}

function modifyStuSubmit(url)
{
	window.open(url);
}

function delStuSubmit(url,name)
{
	if(confirm('确定删除学生 '+name+' ?'))
	{
		location.href(url);
	}
}

function checkStuAdd(form)
{
	if(form.stuId.value == null || form.stuId.value == "")
	{
		alert("学号不能为空");
		return false;
	}
	if(form.name.value == null || form.name.value == "")
	{
		alert("姓名不能为空");
		return false;
	}
	return true;	
}

function roomSubmit(url)
{
	window.open(url);
}

function modifyRoomSubmit(url)
{
	window.open(url);
}

function delRoomSubmit(url,name)
{
	if(confirm('确定删除房间 '+name+' ?'))
	{
		location.href(url);
	}
}

function modifyRecSubmit(url)
{
	window.open(url);
}

function delRecSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}

function modifyRoomDetailSubmit(url)
{
	window.open(url);
}

function delRoomDetailSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}

function roomDetailStateChange(stateId,endInputId)
{
	var state = document.getElementById(stateId).value;
	var endInput = document.getElementById(endInputId);
	if(state == '0') //正在住宿
	{
		endInput.setAttribute("class", "collapse"); //隐藏退住时间输入框
	}
	else
	{
		endInput.setAttribute("class", "collapse in"); //显示
	}
}

function modifyStudentRoomSubmit(url)
{
	window.open(url);
}

function delStudentRoomSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}

function rotaAptEmpSubmit(url)
{
	window.open(url);
}

function delAptEmpSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}

function modifyRotaSubmit(url)
{
	window.open(url);
}

function delRotaSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}

function rotaEmpAptSubmit(url)
{
	window.open(url);
}

function delEmpAptSubmit(url,id)
{
	if(confirm('确定删除记录 '+id+' ?'))
	{
		location.href(url);
	}
}