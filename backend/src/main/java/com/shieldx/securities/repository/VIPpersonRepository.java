package com.shieldx.securities.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.VipPerson;

public interface VIPpersonRepository extends JpaRepository<VipPerson, Integer> {

	// Method to find VIP person by name
	Optional<VipPerson> findByName(String name);

	// Method to find VIP person by email
	Optional<VipPerson> findByEmail(String email);

	// Method to find VIP person by phone number
	Optional<VipPerson> findByPhoneNumber(String phoneNumber);

	// Method to find VIP person by date of birth 	  
 	  	List<VipPerson> findByDateOfBirth(LocalDate dateOfBirth);
	
}
