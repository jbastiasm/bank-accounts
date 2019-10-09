package com.bastiasj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Restriction;

@Repository
public interface RestrictionsRepository extends CrudRepository<Restriction, Long> {

	@Query("UPDATE Restriction r SET r.delete = ?2, r.list = ?3, r.update = ?4 WHERE r.id = ?1")
	public Restriction updateRestriction(Long id, boolean delete, boolean list, boolean update);
}
