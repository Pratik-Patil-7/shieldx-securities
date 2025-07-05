package com.shieldx.securities.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

//	Login findByEmail(String email);

//	Login findByUsername(String username);
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Login> findByUsername(String username);


	Optional<Login> findByEmail(String email);

//	@Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
//	Optional<Login> findByUserId(@Param("userId") Integer userId);
	
    @Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
    Optional<Login> findByUserId(Integer userId);
}
