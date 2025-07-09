package com.karthik.projects.airBnbApp.controllers;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;
import com.karthik.projects.airBnbApp.dtos.HotelSearchRequestDTO;
import com.karthik.projects.airBnbApp.services.InventoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowserController {
    private InventoryService inventoryService;
    @GetMapping("/search")
    public ResponseEntity<Page<HotelDTO>> searchHotels(@RequestBody HotelSearchRequestDTO hotelSearchRequestDTO){
        Page<HotelDTO> page = inventoryService.searchHotels(hotelSearchRequestDTO);
        return ResponseEntity.ok(page);
    }
}
