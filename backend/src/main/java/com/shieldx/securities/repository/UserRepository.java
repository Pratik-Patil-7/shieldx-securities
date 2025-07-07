package com.shieldx.securities.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.mobile = :mobile")
	Optional<User> findByMobile(String mobile);
	
//	User findByUsername(String username);
	
	
	@Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findById(Integer userId);




}
