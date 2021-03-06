package com.jy.pc.Service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jy.pc.DAO.ClassificationDao;
import com.jy.pc.Entity.ClassificationEntity;
import com.jy.pc.Service.ClassificationService;
import com.jy.pc.Utils.DbLogUtil;

@Service
public class ClassificationServiceImpl implements ClassificationService {

	@Autowired
	private ClassificationDao classificationDao;
	@Autowired
	DbLogUtil logger;
	@Autowired
	private ClassificationService classificationService;

	// 分类添加
	@Override
	@Transactional
	public ClassificationEntity save(ClassificationEntity classificationEntity) {
		ClassificationEntity result = classificationDao.saveAndFlush(classificationEntity);
		logger.initAddLog(classificationEntity);
		return result;
	}

	// 分类状态设置 -- 启用禁用
	@Override
	@Transactional
	public ClassificationEntity enable(ClassificationEntity classificationEntity, boolean result) {
		logger.initEnableLog(classificationEntity, result);
		return classificationDao.saveAndFlush(classificationEntity);
	}

	// 分类修改
	@Override
	@Transactional
	public ClassificationEntity update(ClassificationEntity classificationEntity) {
		logger.initUpdateLog(classificationEntity);
		return classificationDao.saveAndFlush(classificationEntity);
	}

	// 分类删除
	@Override
	@Transactional
	public void delete(String id) {
		logger.initDeleteLog(classificationDao.findBId(id));
		classificationDao.deleteById(id);
	}

	// 分类查找
	@Override
	public ClassificationEntity findBId(String id) {

		return classificationDao.findBId(id);
	}

	// 分类查询所有
	@Override
	public List<ClassificationEntity> findAll() {
		return classificationDao.findAll();

	}

	// 上级分类编码
	@Override
	public List<ClassificationEntity> findSubClassiList() {
		return classificationDao.findSubClassiList();

	}

	// 关键词关联分类
	@Override
	public List<ClassificationEntity> findKeyWordList(String classCode) {
		return classificationDao.findKeyWordList(classCode);
	}

	// 病虫害关联分类
	@Override
	public List<ClassificationEntity> findDipList(String classCode) {

		return classificationDao.findDipList(classCode);
	}

	// 农作物关联分类
	@Override
	public List<ClassificationEntity> findCaseList(String classCode) {

		return classificationDao.findCaseList(classCode);
	}

	// 分类关联看图识病农作物删除
	@Override
	public List<ClassificationEntity> findCropLink() {

		return classificationDao.findCropLink();
	}

	// 分类关联看图识病病虫害删除
	@Override
	public List<ClassificationEntity> findDipLink() {

		return classificationDao.findDipLink();
	}

	// 分类关联看图识病关键词删除
	@Override
	public List<ClassificationEntity> findKeywordLink() {

		return classificationDao.findKeywordLink();
	}

	@Override
	public boolean findParentCode(String parentCode) {
		int count = classificationDao.findParentCode(parentCode);
		return count > 0 ? true : false;
	}

	// 分类分页与模糊查询
	@Override
	public Page<ClassificationEntity> findListByName(String code, String name, Pageable pageable) {
		String classiCode = "%" + code + "%";
		String classiName = "%" + name + "%";
		return classificationDao.findListByName(classiCode, classiName, pageable);
	}

	@Override
	public List<ClassificationEntity> findClassByCode(String classCode) {
		return classificationDao.findClassByCode(classCode);
	}

	// 查询子菜单
	@Override
	public List<ClassificationEntity> findListById(String id) {
		return classificationDao.findListById(id);
	}

	// 分类删除
	@Override
	public int deleteClass(String id) {
		List<ClassificationEntity> classi = classificationService.findCropLink();// 农作物
		List<ClassificationEntity> classiFicat = classificationService.findDipLink();// 病虫害
		List<ClassificationEntity> classiFication = classificationService.findKeywordLink();// 关键词
		ClassificationEntity classificationEntity = classificationService.findBId(id);
		int c = 0;
		for (int i = 0; i < classi.size(); i++) {
			ClassificationEntity calssiFicat = classi.get(i);
			if (classificationService.findParentCode(id)) {
				c = 1;
				return c;
			} else if (classificationEntity.getId().equals(calssiFicat.getId())) {
				for (int j = 0; j < classiFicat.size(); j++) {
					ClassificationEntity ficat = classiFicat.get(j);
					if (classificationEntity.getId().equals(ficat.getId())) {
						for (int k = 0; k < classiFication.size(); k++) {
							ClassificationEntity calssiFicati = classiFication.get(k);
							if (classificationEntity.getId().equals(calssiFicati.getId())) {
								classificationService.delete(id);
								c = 2;
							} else {
							}
						}
						if (classiFication.size() <= 0) {
						}
					} else {
					}
				}
				if (classiFicat.size() <= 0) {
				}
			} else {
			}
		}
		if (classi.size() <= 0) {
		}
		return c;
	}
}
