package com.bastiasj.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Restriction;

@Repository
public interface RestrictionsRepository extends CrudRepository<Restriction, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE Restriction r SET r.delete = ?2, r.list = ?3, r.update = ?4 WHERE r.id = ?1")
	public Integer updateRestriction(Long id, boolean delete, boolean list, boolean update) throws Exception;
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Restriction r WHERE r.admin.id = ?1 AND r.user.id = ?2")
	public Integer deleteRestriction(Long adminId, Long userId) throws Exception;
	
	@Query("SELECT r FROM Restriction r WHERE r.admin.id = ?1 AND r.user.id = ?2")
	public Restriction getRestriction(Long adminId, Long userId) throws Exception;
}
