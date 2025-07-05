package com.shieldx.securities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shieldx.securities.model.Bouncer;

public interface BouncerRepository extends JpaRepository<Bouncer, Integer> {

}
