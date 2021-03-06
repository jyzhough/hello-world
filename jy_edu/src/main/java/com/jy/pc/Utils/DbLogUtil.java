package com.jy.pc.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jy.pc.DAO.DbLogInfoDao;
import com.jy.pc.Entity.DbLogInfoEntity;
/**
 *  再service中涉及修改、删除、新增、状态设置的代码中
 *  调用此类中的对应方法，
 *
 *
 */
@Service
public class DbLogUtil {

	@Autowired
	private DbLogInfoDao dbLogInfoDao;
	
	/**
	 *	  删除实体时调用
	 * note:
	 */
	public void initDeleteLog(Object object) {
		Table[] tables = object.getClass().getAnnotationsByType(Table.class);
		if(tables.length > 0) {		
			saveLog(tables[0].name()," 删除 ",JSON.toJSONString(object));;
		}
	}

	public void initLoginLog(Object object) {
		saveLog("全局"," 登录 ",JSON.toJSONString(object));;
	}
	
	public void initAddLog(Object object) {
		Table[] tables = object.getClass().getAnnotationsByType(Table.class);
		if(tables.length > 0) {		
			saveLog(tables[0].name()," 新增 ",JSON.toJSONString(object));;
		}
	}
	
	public void initUpdateLog(Object object) {
		Table[] tables = object.getClass().getAnnotationsByType(Table.class);
		if(tables.length > 0) {		
			saveLog(tables[0].name()," 修改 ",JSON.toJSONString(object));;
		}
	}
	
	/**
	 * 
	 * @param object 操作的实体
	 * @param result 启用为true 禁用为false
	 * note: 部分表状态为0代表启用，部分表状态为1代表启用，因此不进行封装
	 */
	public void initEnableLog(Object object,boolean result) {
		Table[] tables = object.getClass().getAnnotationsByType(Table.class);
		if(tables.length > 0) {		
			saveLog(tables[0].name(),result?"启用 -- 状态切换":"禁用 -- 状态切换",JSON.parseObject(JSON.toJSONString(object)).getString("id"));;
		}
	}
	
	public void initAuditLog(Object object,boolean result) {
		Table[] tables = object.getClass().getAnnotationsByType(Table.class);
		if(tables.length > 0) {		
			saveLog(tables[0].name(),result?" 审核通过 ":" 审核驳回 ",JSON.toJSONString(object));;
		}
	}
	
	private void saveLog(String module,String action,String obj) {
		Aes aes = new Aes();
		String result = "";
		if(obj!=null && obj.length()>65000) {
			obj = obj.substring(0,65000);
		}
		DbLogInfoEntity entity = new DbLogInfoEntity();
		entity.setAction(action);
		entity.setActObj(obj);
		entity.setLogDate(nowDateTime());
		entity.setModule(castTableToEntity(module));
		dbLogInfoDao.save(entity);
	}
	
	public static Date nowDateTime(){
	      return Timestamp.valueOf(
	            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(new Date())
	      );
	}
	
	/**
	 *	自定义日志
	 * @param module 功能模块
	 * @param action 操作
	 * @param remark 备注
	 * consumer:
	 * note:
	 */
	public void initCustomizedLog(String module,String action,Object remark) {
		saveLog(module, action, JSON.toJSONString(remark));
	}
	
	private static String castTableToEntity(String tableName) {
		String result ;
		switch (tableName) {
		case "sas_account_info": 
			result = "账户管理";
			break;
		case "sas_agricultural_clothing_info": 
			result = "农服管理";
			break;
		case "sas_publication_info": 
			result = "农服管理";
			break;
		case "sas_case_info": 
			result = "看图识病";
			break;
		case "sas_classification_info": 
			result = "分类管理";
			break;
		case "sas_comment_reply_info": 
			result = "回复管理";
			break;
		case "sas_db_log_info": 
			result = "日志管理";
			break;
		case "sas_key_word": 
			result = "关键词管理";
			break;
		case "sas_menu": 
			result = "菜单管理";
			break;
		case "sas_module_info": 
			result = "模块管理";
			break;
		case "sas_post_comment_info": 
			result = "评论管理";
			break;
		case "sas_post_info": 
			result = "贴子管理";
			break;
		case "sas_power_info": 
			result = "权限管理";
			break;
		case "sas_role": 
			result = "角色管理";
			break;
		case "sas_dict_type": 
			result = "字典管理(类型)管理";
			break;
		case "sas_dict_data": 
			result = "字典管理(键值)管理";
			break;
		case "sas_deploy_module_info":
			result = "发布信息模块管理";
			break;
		default:
			result = tableName;
			break;
		}
		return result;
	}
	
	
}
