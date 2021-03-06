package com.jy.pc.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jy.pc.Entity.ArticleManageEntity;
import com.jy.pc.Entity.SectionManageEntity;
import com.jy.pc.Service.ArticleManageService;

/**
 * 文章管理controller
 */
@Controller
@RequestMapping(value = "/articleManage")
public class ArticleManageController {

	@Autowired
	private ArticleManageService eduArticleManageService;

	// 查询文章管理信息
	@RequestMapping(value = "/findByName")
	@ResponseBody
	public Map<String, Object> findByName(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "title") String title, @RequestParam(name = "sectionId") String sectionId,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {

		Map<String, Object> map = new HashMap<String, Object>();
		Pageable pageable = new PageRequest(page - 1, size);
		Page<ArticleManageEntity> articleManageList = eduArticleManageService.findListByName(title, sectionId,
				pageable);
		try {
			map.put("state", "0");// 成功
			map.put("message", "查询成功");
			map.put("data", articleManageList);
		} catch (Exception e) {
			map.put("state", "1");
			map.put("message", "查询失败,请重新查询");
		}
		
		return map;
	}

	// 添加文章管理信息
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, String> save(HttpServletRequest res, HttpServletResponse req) {
		Map<String, String> map = new HashMap<String, String>();
		String s = res.getParameter("eduArticleManageEntity");
		JSONObject jsonObject = JSONObject.parseObject(s);
		ArticleManageEntity eduArticleManageEntity = jsonObject.toJavaObject(ArticleManageEntity.class);

		SectionManageEntity sectionManage = new SectionManageEntity();
		
		sectionManage.setId(eduArticleManageEntity.getSectionId());//关联版块id
		eduArticleManageEntity.setSection(sectionManage);

		Date date = new Date();
		eduArticleManageEntity.setCreateDate(date);
		eduArticleManageEntity.setStatus(1);
		try {
			eduArticleManageService.save(eduArticleManageEntity);
			map.put("state", "0");
			map.put("message", "添加成功");
		} catch (Exception e) {
			map.put("state", "1");
			map.put("message", "添加失败，请重新添加");
		}
		
		return map;
	}

	// 通过id查询文章管理信息
	@RequestMapping(value = "/findById")
	@ResponseBody
	public Map<String, Object> findById(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		ArticleManageEntity eduArticleManageEntity = eduArticleManageService.findBId(id);
		try {
			map.put("state", "0");
			map.put("message", "查询成功");
			map.put("data", eduArticleManageEntity);
		} catch (Exception e) {
			map.put("state", "1");
			map.put("message", "查询失败，请刷新或联系管理员");
		}
		
		return map;
	}

	// 修改文章管理信息
	@RequestMapping(value = "/update")
	@ResponseBody
	public Map<String, String> update(HttpServletRequest res, HttpServletResponse req) {

		Map<String, String> map = new HashMap<String, String>();
		String s = res.getParameter("eduArticleManageEntity");
		JSONObject jsonObject = JSONObject.parseObject(s);
		ArticleManageEntity eduArticleManageEntity = jsonObject.toJavaObject(ArticleManageEntity.class);

		SectionManageEntity sectionManage = new SectionManageEntity();
		
		sectionManage.setId(eduArticleManageEntity.getSectionId());//关联版块id
		eduArticleManageEntity.setSection(sectionManage);
		eduArticleManageEntity.setStatus(1);
		eduArticleManageService.update(eduArticleManageEntity);
		try {
			map.put("state", "0");
			map.put("message", "修改成功");
		} catch (Exception e) {
			map.put("state", "1");
			map.put("message", "修改失败，请检查修改内容是否有误");
		}
		return map;
	}

	// 删除文章管理信息
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "id") String id) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			eduArticleManageService.delete(id);
			map.put("state", "0");
			map.put("message", "删除成功");
		} catch (Exception e) {
			map.put("state", "1");
			map.put("message", "删除失败");
		}
		
		return map;
	}

	// 启用/禁用文章管理信息
	@RequestMapping(value = "/enable")
	@ResponseBody
	public Map<String, String> opensulf(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "status") Integer status, @RequestParam(name = "id") String id,
			@RequestParam(name = "updateBy") String updateBy) {
		Date date = new Date();
		Map<String, String> map = new HashMap<String, String>();
		ArticleManageEntity eduArticleManageEntity = eduArticleManageService.findBId(id);
		eduArticleManageEntity.setStatus(status);
		eduArticleManageEntity.setUpdateDate(date);
		eduArticleManageEntity.setUpdateBy(updateBy);
		boolean result = true;
		// 启用
		if (status.equals(0)) {
			map.put("state", "0");
			map.put("message", "启用成功");
		}
		// 禁用
		if (status.equals(1)) {
			map.put("state", "1");
			map.put("message", "禁用成功");
			result = false;
		}
		eduArticleManageService.enable(eduArticleManageEntity, result);
		return map;
	}

	// 移动端-查询文章管理信息的最新3条有效记录（接口）
	@RequestMapping(value = "/findArticleInfo")
	@ResponseBody
	public Map<String, Object> findArticleInfo(HttpServletRequest res, HttpServletResponse req) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<ArticleManageEntity> eduArticleManageEntity = eduArticleManageService.findArticleInfo();
		try {
			map.put("code", "200");// 查询成功
			map.put("message", "查询成功");
			map.put("data", eduArticleManageEntity);
		} catch (Exception e) {
			map.put("code", "201");// 查询失败
			map.put("message", "查询失败");
		}
		return map;
	}

	// 移动端-条件查询文章管理信息列表（接口）
	@RequestMapping(value = "/findListByChoose")
	@ResponseBody
	public Map<String, Object> findListByChoose(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "sectionId") String sectionId, @RequestParam(name = "page") Integer page,
			@RequestParam(name = "size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		Pageable pageable = new PageRequest(page - 1, size);
		Page<ArticleManageEntity> articleList = eduArticleManageService.findListByChoose(sectionId, pageable);
		try {
			map.put("code", "200");// 查询成功
			map.put("message", "查询成功");
			map.put("data", articleList);
		} catch (Exception e) {
			map.put("code", "201");// 查询失败
			map.put("message", "查询失败");
		}
		return map;
	}
	
	// 移动端-根据Id查询文章管理列表详情（接口）
	@RequestMapping(value="/findArticleId")
	@ResponseBody
	public Map<String,Object> findArticleId(HttpServletRequest res,HttpServletResponse req,@RequestParam(name = "id") String id){
		Map<String,Object> map = new HashMap<String, Object>();
		ArticleManageEntity articleManageEntity = eduArticleManageService.findBId(id);
		try {
			map.put("code", "200");//查询成功
			map.put("message", "查询成功");
			map.put("data", articleManageEntity);
		} catch (Exception e) {
			map.put("code", "201");//查询失败
			map.put("message", "查询失败");
		}
		return map;
		
	}
}
