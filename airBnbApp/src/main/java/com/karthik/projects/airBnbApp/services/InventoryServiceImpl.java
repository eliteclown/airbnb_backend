package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;
import com.karthik.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.karthik.projects.airBnbApp.entities.Hotel;
import com.karthik.projects.airBnbApp.entities.Inventory;
import com.karthik.projects.airBnbApp.entities.Room;
import com.karthik.projects.airBnbApp.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

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
        log.info("Deleting the inventories of room with id : {}", room.getId());
        LocalDate today =LocalDate.now();
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelDTO> searchHotels(HotelSearchRequestDTO hotelSearchRequestDTO) {

        log.info("searching hotels for {} city,from {} to {}",hotelSearchRequestDTO.getCity(),hotelSearchRequestDTO.getStartDate(),hotelSearchRequestDTO.getEndDate());

        Pageable pageable = PageRequest.of(hotelSearchRequestDTO.getPage(), hotelSearchRequestDTO.getSize());
        LocalDate startDate = hotelSearchRequestDTO.getStartDate();
        LocalDate endDate = hotelSearchRequestDTO.getEndDate();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        Long dateCount = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        Page<Hotel> hotelPage = inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequestDTO.getCity(),
                hotelSearchRequestDTO.getStartDate(),hotelSearchRequestDTO.getEndDate(),
                hotelSearchRequestDTO.getRoomsCount(),dateCount,pageable);

        return hotelPage.map(hotel -> modelMapper.map(hotel, HotelDTO.class));
    }
}
