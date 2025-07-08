package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.entities.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
