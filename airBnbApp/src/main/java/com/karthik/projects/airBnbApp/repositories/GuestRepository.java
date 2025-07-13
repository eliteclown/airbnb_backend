package com.karthik.projects.airBnbApp.repositories;

import com.karthik.projects.airBnbApp.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest,Long> {
}
