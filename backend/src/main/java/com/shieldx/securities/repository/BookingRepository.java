package com.shieldx.securities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId")
    List<Booking> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId AND b.status = :status")
    List<Booking> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);
   

}

