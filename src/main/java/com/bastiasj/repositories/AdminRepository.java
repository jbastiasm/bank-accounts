package com.bastiasj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {

	@Query("SELECT a FROM Admin a WHERE a.login = ?1")
	public Admin findAdminByLogin(String login);
}
