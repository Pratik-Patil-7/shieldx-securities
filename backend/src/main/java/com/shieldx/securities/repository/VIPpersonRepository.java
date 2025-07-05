package com.shieldx.securities.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.VipPerson;

<<<<<<< HEAD:backend/src/main/java/com/shieldx/securities/repository/VipPersonRepository.java
@Repository
public interface VipPersonRepository extends JpaRepository<VipPerson, Integer> {
=======
public interface VIPpersonRepository extends JpaRepository<VipPerson, Integer> {
>>>>>>> 6ff7aa185b303b4620bebe1bd219edf3445c916e:backend/src/main/java/com/shieldx/securities/repository/VIPpersonRepository.java
    List<VipPerson> findByDateOfBirth(LocalDate dateOfBirth);
    Optional<VipPerson> findByEmail(String email);
}
