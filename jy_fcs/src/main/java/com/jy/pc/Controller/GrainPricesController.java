package com.jy.pc.Controller;

import java.util.Date;
import java.util.HashMap;
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
import com.jy.pc.Entity.GrainPricesEntity;
import com.jy.pc.Entity.GrainPricesHistoryEntity;
import com.jy.pc.Entity.ModuleInfoEntity;
import com.jy.pc.Service.GrainPricesHistoryService;
import com.jy.pc.Service.GrainPricesService;

@Controller
@RequestMapping(value="/grainPrices")
public class GrainPricesController {
	
	@Autowired
	private GrainPricesService grainPricesService;
	
	@Autowired
	private GrainPricesHistoryService grainPricesHistoryService;
	
	@RequestMapping(value="/saveOrUpdate")
	@ResponseBody
	public Map<String,Object> saveOrUpdate(HttpServletRequest res, HttpServletResponse req){
		Map<String,Object> map =new HashMap<String,Object>();
		String s = res.getParameter("grainPricesEntity");
		JSONObject jsonObject = JSONObject.parseObject(s);
		GrainPricesEntity grainPricesEntity = jsonObject.toJavaObject(GrainPricesEntity.class);
		grainPricesEntity.setCreateDate(new Date());
		grainPricesEntity.setPriceDefinedType("0");
		grainPricesService.saveOrUpdate(grainPricesEntity);//保存粮价数据
		
		GrainPricesHistoryEntity grainPricesHistoryEntity = new GrainPricesHistoryEntity();
		if(grainPricesEntity.getId()!=null) {
			grainPricesHistoryEntity.setOperateType("0");//0新增
			grainPricesHistoryEntity.setOperateContent("操作员："+grainPricesEntity.getCreateUser()+"于"+new Date()+"新增一条粮价数据");
		}else {
			grainPricesHistoryEntity.setOperateType("1");//1修改
			grainPricesHistoryEntity.setOperateContent("操作员："+grainPricesEntity.getCreateUser()+"于"+new Date()+"修改一条粮价数据");
		}
		grainPricesHistoryEntity.setCreateDate(new Date());
		grainPricesHistoryService.saveOrUpdate(grainPricesHistoryEntity);//保存操作历史
		
		map.put("status", "0");
		map.put("message", "保存成功");
		return map;
		
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Map<String,Object> delete(HttpServletRequest res, 
			HttpServletResponse req,@RequestParam("id") String id,
			@RequestParam("currentUser") String currentUser){
		
		Map<String,Object> map =new HashMap<String,Object>();
		grainPricesService.delete(id);
		
		GrainPricesEntity grainPricesEntity = grainPricesService.findInfoById(id);//根据id查询数据
		GrainPricesHistoryEntity grainPricesHistoryEntity = new GrainPricesHistoryEntity();
		grainPricesHistoryEntity.setOperateType("2");// 2删除
		grainPricesHistoryEntity.setOperateContent("操作员："+currentUser+"于"+new Date()+"删除一条"+grainPricesEntity.getPriceDate()+"的粮价数据");
		grainPricesHistoryEntity.setCreateDate(new Date());
		grainPricesHistoryService.saveOrUpdate(grainPricesHistoryEntity);//保存操作历史
		map.put("status", "0");
		map.put("message", "删除成功");
		return map;
		
	}
	
	@RequestMapping(value = "/findById")
	@ResponseBody
	public Map<String, Object> findById(HttpServletRequest res, HttpServletResponse req,
			@RequestParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		GrainPricesEntity grainPricesEntity = grainPricesService.findInfoById(id);
		map.put("status", "0");
		map.put("message", "查询成功");
		map.put("data", grainPricesEntity);
		return map;

	}
	
	// 根据参数查询 分页
	@RequestMapping(value = "/findPageByParam")
	@ResponseBody
	public Map<String, Object> findPageByParam(HttpServletRequest res, HttpServletResponse req,
			@RequestParam(name = "priceDefinedType") String priceDefinedType,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {

		Map<String, Object> map = new HashMap<String, Object>();
		Pageable pageable = new PageRequest(page - 1, size);
		Page<GrainPricesEntity> grainPricesList = grainPricesService.findPageByParam(priceDefinedType, pageable);
		map.put("state", "0");// 成功
		map.put("message", "查询成功");
		map.put("data", grainPricesList);
		return map;
	}

}