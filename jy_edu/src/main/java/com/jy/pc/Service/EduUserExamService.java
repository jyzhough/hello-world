package com.jy.pc.Service;

import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;

import com.jy.pc.Entity.EduUserExamEntity;

public interface EduUserExamService {
	
	public void save(EduUserExamEntity eduUserExamEntity) throws ServiceException;
	
	public List<Map<String,Object>> getExamResultByUserId(String userId) throws ServiceException;

	public void deleteByExam(String userId,String examId);
	
	public EduUserExamEntity isPass(String userId,String examId);
	
	//根据职业类别和通过状态查询数据
	public List<EduUserExamEntity> findByVocation(String vocationId,String isPass);
	
	public List<Map<String,Object>> getExamIsPassByUserId(String userId) throws ServiceException;
	
}
