package com.shieldx.securities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.Login;
import java.util.List;


public interface LoginRepository extends JpaRepository<Login, Integer> {

	Login findByEmail(String email);
	Login findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
