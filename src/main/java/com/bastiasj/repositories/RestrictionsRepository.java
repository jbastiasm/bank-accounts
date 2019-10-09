package com.bastiasj.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bastiasj.entities.Restriction;

@Repository
public interface RestrictionsRepository extends CrudRepository<Restriction, Long> {


}
