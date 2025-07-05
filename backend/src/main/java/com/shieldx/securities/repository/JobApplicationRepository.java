package com.shieldx.securities.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shieldx.securities.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

	@Query("SELECT ja FROM JobApplication ja WHERE ja.user.userId = :userId")
	Optional<JobApplication> findByUserId(@Param("userId") Integer userId);


}