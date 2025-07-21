package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.Strategy.PricingService;
import com.karthik.projects.airBnbApp.entities.Hotel;
import com.karthik.projects.airBnbApp.entities.HotelMinPrice;
import com.karthik.projects.airBnbApp.entities.Inventory;
import com.karthik.projects.airBnbApp.repositories.HotelMinPriceRepository;
import com.karthik.projects.airBnbApp.repositories.HotelRepository;
import com.karthik.projects.airBnbApp.repositories.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    // Scheduler to update the inventory and HotelMinPrice tables every hour

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0/5 * * * * *")
    public void updatePrices(){

        int page =0;
        int batchSize =100;

        while (true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page,batchSize));
            if(hotelPage.isEmpty()){
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);
            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel){
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);
        updateInventoryPrices(inventoryList);
        updateHotelMinPrice(hotel,inventoryList,startDate,endDate);

    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {

        // Compute minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,e -> e.getValue().orElse(BigDecimal.ZERO)));

        //prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date,price)->{
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        //save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList){
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }
}
