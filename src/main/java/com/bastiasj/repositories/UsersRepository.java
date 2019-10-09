package com.bastiasj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {

	@Query("DELETE FROM User u WHERE u.id = ?1")
	public void deleteUserById(Long id);
	
	@Query("UPDATE User u SET u.firstName = ?2, u.lastName = ?3, u.iban = ?4 WHERE u.id = ?1")
	public void updateUserById(Long id, String firstName, String lastName, String iban);
	
	@Query("SELECT u FROM User u WHERE u.admin.id = ?1")
	public List<User> findAllUsersByAdmin(Long adminId);
}
