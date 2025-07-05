package com.shieldx.securities.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "job_applications")
@Data
public class JobApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicationId;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String name;
	private String email;
	private String mobile;
	private LocalDate dob;
	private String gender;
	private String address;
	private String qualification;
	private String experience;
	private String resumeUrl;
	private String photoUrl;
	private String status = "pending";
}
