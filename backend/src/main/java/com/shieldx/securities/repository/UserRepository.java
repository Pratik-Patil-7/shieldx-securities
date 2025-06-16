package com.shieldx.securities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
