package com.shieldx.securities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.SecurityType;


@Repository
public interface SecurityTypeRepository extends JpaRepository<SecurityType, Integer>{

}
