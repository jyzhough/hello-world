package com.jy.pc.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jy.pc.Entity.RoleMenuRelationEntity;

public interface RoleMenuRelationService {

	// 添加
	public RoleMenuRelationEntity save(RoleMenuRelationEntity roleMenuRelationEntity);

	// 修改
	public void update(RoleMenuRelationEntity roleMenuRelationEntity);

	// 删除
	public void delete(String id);

	// 主鍵查詢
	public RoleMenuRelationEntity findId(String id);

	// 根据角色查询
	public List<RoleMenuRelationEntity> findRelationByRole(String roleId);

	// 根据菜单查询
	public List<RoleMenuRelationEntity> findRelationByMenu(String menuId);

	// 查询关联表数据合法性
	public int checkRealtion(String roleId, String menuId);

	// 查询菜单挂载情况
	public boolean hasRelationByMenu(String menuId);

	// 查询角色挂载情况
	public boolean hasRelationByRole(String roleId);

	//批量保存角色授权信息
	public void batchSave(String roleId, String idArr);
	
	//清空角色权限
	public void deleteByRole(String roleId);
}
