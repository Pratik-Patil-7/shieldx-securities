package com.shieldx.securities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.Login;

public interface LoginRepository extends JpaRepository<Login, Integer> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

}
