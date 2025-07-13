package com.karthik.projects.airBnbApp.controllers;

import com.karthik.projects.airBnbApp.dtos.BookingDTO;
import com.karthik.projects.airBnbApp.dtos.BookingRequestDTO;
import com.karthik.projects.airBnbApp.dtos.GuestDTO;
import com.karthik.projects.airBnbApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDTO> initialiseBooking(@RequestBody BookingRequestDTO bookingRequestDTO){
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequestDTO));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDTO> addGuest(@PathVariable Long bookingId, @RequestBody List<GuestDTO> guestDTOList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDTOList));
    }
}
