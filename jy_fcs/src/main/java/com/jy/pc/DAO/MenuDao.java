package com.jy.pc.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jy.pc.Entity.MenuEntity;

public interface MenuDao extends JpaRepository<MenuEntity, String> {
	@Query(value = "select * from sas_menu t where t.id =:id", nativeQuery = true)
	public MenuEntity findId(@Param("id") String id);

	@Query(value = "select a.* from sas_menu a where FIND_IN_SET(a.id,getParentList((select group_concat(t.id) from sas_menu t where if(?1 !='',t.name like ?1,1=1) ORDER BY sort ASC))) ORDER BY sort ASC", nativeQuery = true)
	public List<MenuEntity> findListByName(String name);

	@Query(value = "select count(0) from sas_menu t where t.parent_id =:parentId", nativeQuery = true)
	public int findSubMenuCount(@Param("parentId") String parentId);

	@Query(value = "select id,name,parent_id,level from sas_menu", nativeQuery = true)
	public List<Map<String,Object>> findTree();

	@Query(value = "select s.* from sas_role_menu_relation t ,sas_menu s where t.role_id=:roleId and t.menu_id=s.id and s.menu_type = 1 and s.status = 0 ORDER BY sort ASC", nativeQuery = true)
	public List<MenuEntity> findIndex(@Param("roleId")String roleId);
	
	@Query(value = "select * from sas_menu t where t.menu_type = 2 and t.status = 0 and t.parent_id =:parentId ORDER BY sort ASC", nativeQuery = true)
	public List<MenuEntity> findByParent(@Param("parentId") String parentId);
}
