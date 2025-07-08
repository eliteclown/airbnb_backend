package com.karthik.projects.airBnbApp.repositories;

import com.karthik.projects.airBnbApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
}
