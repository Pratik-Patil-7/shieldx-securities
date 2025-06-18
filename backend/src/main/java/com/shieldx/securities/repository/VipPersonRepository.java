package com.shieldx.securities.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.VipPerson;

public interface VipPersonRepository extends JpaRepository<VipPerson, Integer> {
    List<VipPerson> findByDateOfBirth(LocalDate dateOfBirth);

}
