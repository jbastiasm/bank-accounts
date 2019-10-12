package com.bastiasj.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM Users u WHERE u.id = ?1")
	public Integer deleteUserById(Long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Users u SET u.firstName = ?2, u.lastName = ?3, u.iban = ?4 WHERE u.id = ?1")
	public Integer updateUserById(Long id, String firstName, String lastName, String iban);
	
	@Query("SELECT u FROM Users u, Restriction r WHERE r.admin.id = ?1 AND u.id = r.user.id")
	public List<Users> findAllUsersByAdmin(Long adminId);
	
	@Query("SELECT u FROM Users u WHERE u.iban = ?1")
	public Users checkDuplicateIBAN(String iban);
	
}
