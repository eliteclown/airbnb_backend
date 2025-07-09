package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;
import com.karthik.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.karthik.projects.airBnbApp.entities.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);

    Page<HotelDTO> searchHotels(HotelSearchRequestDTO hotelSearchRequestDTO);
}
