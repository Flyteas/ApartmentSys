package com.flyteas.ApartmentSys.Service;

import java.util.List;

import com.flyteas.ApartmentSys.Domain.Rota;

/* 值班业务接口 */
public interface RotaSrv 
{
	public Rota getById(String rotaId);
	public boolean addRota(Rota rota); //添加
	public int modifyRota(Rota rota,long startTime,long endTime); //修改 成功返回0 开始时间错误-1 结束时间错误-2 开始时间大于结束时间-3 数据库错误-4
	public int delRota(String rotaId); //删除，成功返回0，记录不存在返回-1 数据库错误返回-2
	public List<Rota> searchByAptTime(String aptId,long startTime,long endTime,int page,int pageSize); //按时间检索公寓楼值班记录 -1表示时间无限制 公寓楼不存在返回null 分页
	public long searchByAptTimeSize(String aptId,long startTime,long endTime); //按时间检索公寓楼值班记录数 -1表示时间无限制 公寓楼不存在返回-1
	public List<Rota> searchByAptEmpTime(String aptEmpId, long startTime, long endTime, int page, int pageSize); //按时间检索公寓员工值班记录 -1表示时间无限制 公寓员工不存在返回null 分页
	public long searchByAptEmpTimeSize(String aptEmpId, long startTime, long endTime); //按时间检索公寓员工值班记录数 -1表示时间无限制 公寓员工不存在返回null
	public List<Rota> searchByEmpTime(String empId, long startTime, long endTime, int page, int pageSize); //按时间检索员工值班记录 -1表示时间无限制 员工不存在返回null 分页
	public long searchByEmpTimeSize(String empId, long startTime, long endTime); //按时间检索员工值班记录数 -1表示时间无限制 员工不存在返回null
}
