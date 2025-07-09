package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;
import com.karthik.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.karthik.projects.airBnbApp.entities.Inventory;
import com.karthik.projects.airBnbApp.entities.Room;
import com.karthik.projects.airBnbApp.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(;!today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotelEntity())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotelEntity().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();

            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteFutureInventories(Room room) {
        LocalDate today =LocalDate.now();
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelDTO> searchHotels(HotelSearchRequestDTO hotelSearchRequestDTO) {

        return null;
    }
}
