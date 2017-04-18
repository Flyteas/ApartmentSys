package com.flyteas.ApartmentSys.Dao;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Apartment;
import com.flyteas.ApartmentSys.Domain.StuAccessRecord;
import com.flyteas.ApartmentSys.Domain.Student;

/* 学生门禁记录 数据访问接口 */
public interface StuAccessRecordDao 
{
	public StuAccessRecord getById(String id); //通过ID获取
	public List<StuAccessRecord> getByStudent(Student student); //获取某学生所有门禁记录
	public StuAccessRecord getLastByStudent(Student student,Apartment apartment); //获取某学生在某公寓最后一条门禁记录
	public List<StuAccessRecord> getByApartment(Apartment apartment); //获取某公寓楼所有记录
	public boolean add(StuAccessRecord stuAccessRecord); //添加
	public boolean saveModify(StuAccessRecord stuAccessRecord); //保存修改
	public boolean delById(String id); //通过ID删除
	public int delByStudent(Student student); //删除某学生所有门禁记录
	public int delByApartment(Apartment apartment); //删除某公寓楼所有记录
}
