package com.karthik.projects.airBnbApp.repositories;

import com.karthik.projects.airBnbApp.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
