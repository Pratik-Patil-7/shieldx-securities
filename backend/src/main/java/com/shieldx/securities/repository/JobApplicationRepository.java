package com.shieldx.securities.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

	@Query("SELECT ja FROM JobApplication ja WHERE ja.user.userId = :userId")
	Optional<JobApplication> findByUserId(@Param("userId") Integer userId);

//	String[] uploadFiles(Integer userId, MultipartFile resume, MultipartFile photo);

//	@Query("SELECT ja FROM JobApplication ja WHERE ja.user.userId = :userId AND ja.status = 'PENDING'")
//	List<JobApplicationResponse> findPendingApplications(@Param("userId") Integer userId);

	@Query("SELECT new com.shieldx.securities.dto.JobApplicationResponse("
			+ "ja.id, ja.dob, ja.user.userId, ja.address, ja.email, ja.experience, ja.gender, ja.mobile, ja.name, ja.photoUrl, ja.qualification, ja.resumeUrl, ja.status) "
			+ "FROM JobApplication ja " + "WHERE ja.user.userId = :userId AND ja.status = 'PENDING'")
	List<JobApplicationResponse> findPendingApplications(@Param("userId") Integer userId);

	@Query("SELECT new com.shieldx.securities.dto.JobApplicationResponse("
			+ "ja.id, ja.dob, ja.user.userId, ja.address, ja.email, ja.experience, ja.gender, ja.mobile, ja.name, ja.photoUrl, ja.qualification, ja.resumeUrl, ja.status) "
			+ "FROM JobApplication ja " + "WHERE ja.id = :id AND ja.user.userId = :userId")
	JobApplicationResponse findByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

}