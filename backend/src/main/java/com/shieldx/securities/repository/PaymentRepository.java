package com.shieldx.securities.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shieldx.securities.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	/**
	 * Finds a payment by its associated booking ID.
	 *
	 * @param bookingId the ID of the booking
	 * @return an Optional containing the Payment if found, or empty if not found
	 */

	@Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId")
	Optional<Payment> findByBookingId(Integer bookingId);
}