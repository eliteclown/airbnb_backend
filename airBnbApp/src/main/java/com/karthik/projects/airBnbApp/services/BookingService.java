package com.karthik.projects.airBnbApp.services;


import com.karthik.projects.airBnbApp.dtos.BookingDTO;
import com.karthik.projects.airBnbApp.dtos.BookingRequestDTO;
import com.karthik.projects.airBnbApp.dtos.GuestDTO;

import java.util.List;

public interface BookingService {
    BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList);
}
