package com.karthik.projects.airBnbApp.services;


import com.karthik.projects.airBnbApp.dtos.BookingDTO;
import com.karthik.projects.airBnbApp.dtos.BookingRequestDTO;
import com.karthik.projects.airBnbApp.dtos.GuestDTO;
import com.stripe.model.Event;

import java.util.List;

public interface BookingService {
    BookingDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList);

    String initiatePayments(Long bookingId);

    void capturePayments(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
