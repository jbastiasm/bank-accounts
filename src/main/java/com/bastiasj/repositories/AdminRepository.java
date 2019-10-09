package com.bastiasj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {

}
