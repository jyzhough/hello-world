package com.jy.pc.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jy.pc.Entity.AccountPowerInfoEntity;

public interface AccountPowerInfoDao extends JpaRepository<AccountPowerInfoEntity, String> {
	@Query(value = "select * from sas_account_power_info t where t.account_id =:accountId", nativeQuery = true)
	// @Query(value="select t.limit_id,d.limit_id from sys_role t inner join
	// sys_relation d on d.limit_id=t.limit_id",nativeQuery = true)
	public List<AccountPowerInfoEntity> findAccountId(@Param("accountId") String accountId);

	@Query(value = "SELECT * FROM sas_account_power_info t where t.account_id =:accountId and t.jur_codel =:jurCodel", nativeQuery = true)
	public AccountPowerInfoEntity findId(@Param("accountId") String accountId, @Param("jurCodel") String jurCodel);

	@Query(value = "delete from sas_account_power_info where jur_codel =:jurCodel", nativeQuery = true)
	@Modifying
	public void deleteByJurCode(@Param("jurCodel") String jurCodel);
	
	
//	@Query(value="delete from sas_account_power_info  where account_id = :accountId and jur_codel =:jurCodel",nativeQuery = true)
//	@Modifying
//	public void deleteByJurCode(@Param("jurCodel") String jurCodel,@Param("accountId") String accountId);
}
