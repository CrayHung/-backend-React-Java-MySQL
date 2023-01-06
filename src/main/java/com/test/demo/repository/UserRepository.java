package com.test.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.*;

@Repository
public interface UserRepository extends CrudRepository<Users,Integer>{
    
}