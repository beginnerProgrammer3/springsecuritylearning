package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Integer> {
}
