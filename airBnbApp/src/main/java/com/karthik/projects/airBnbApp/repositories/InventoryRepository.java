package com.karthik.projects.airBnbApp.repositories;

import com.karthik.projects.airBnbApp.entities.Inventory;
import com.karthik.projects.airBnbApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    void deleteByDateAndRoom(LocalDate today, Room room);
}
