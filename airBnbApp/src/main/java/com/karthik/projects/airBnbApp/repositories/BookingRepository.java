package com.karthik.projects.airBnbApp.repositories;

import com.karthik.projects.airBnbApp.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
   Optional< Booking> findByPaymentSessionId(String sessionId);
}
