package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.dtos.HotelDTO;

public interface HotelService {
    HotelDTO createNewHotel(HotelDTO hotelDTO);
    HotelDTO getHotelById(Long id);
    HotelDTO updateHotelById(Long id,HotelDTO hotelDTO);
    void deleteHotelById(Long id);
    void activateHotelById(Long id);

}
