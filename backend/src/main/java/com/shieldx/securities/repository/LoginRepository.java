package com.shieldx.securities.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.dto.UserWithStatusResponse;
import com.shieldx.securities.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

//	Login findByEmail(String email);

//	Login findByUsername(String username);
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	@Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
	Optional<Login> findByUserId(Long userId); // Add this method

	@Query("SELECT l FROM Login l WHERE l.username = :username")
	Optional<Login> findByUsername(String username);

	@Query("SELECT l FROM Login l WHERE l.email = :email")
	Optional<Login> findByEmail(String email);

//	@Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
//	Optional<Login> findByUserId(@Param("userId") Integer userId);

	@Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
	Optional<Login> findByUserId(Integer userId);

	@Query("SELECT l FROM Login l WHERE l.user.userId = :userId")
	Optional<Login> findByUserUserId(Integer userId);

	@Query("SELECT new com.shieldx.securities.dto.UserWithStatusResponse(u.userId, u.firstName, u.lastName, u.email, u.mobile, u.address, l.status) "
			+ "FROM User u JOIN Login l ON u.userId = l.user.userId")
	List<UserWithStatusResponse> findAllWithUser();

	@Query("SELECT l FROM Login l JOIN FETCH l.user WHERE l.user.userId = :userId")
	Optional<Login> findLoginWithUserByUserId(@Param("userId") Integer userId);
}
