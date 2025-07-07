package com.shieldx.securities.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shieldx.securities.model.VipPerson;

public interface VIPpersonRepository extends JpaRepository<VipPerson, Integer> {

    @Query("SELECT v FROM VipPerson v WHERE v.name = :name")
    Optional<VipPerson> findByName(@Param("name") String name);

    @Query("SELECT v FROM VipPerson v WHERE v.email = :email")
    Optional<VipPerson> findByEmail(@Param("email") String email);

    @Query("SELECT v FROM VipPerson v WHERE v.mobile = :phoneNumber")
    Optional<VipPerson> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT v FROM VipPerson v WHERE v.dateOfBirth = :dob")
    List<VipPerson> findByDateOfBirth(@Param("dob") LocalDate dateOfBirth);
	
}
