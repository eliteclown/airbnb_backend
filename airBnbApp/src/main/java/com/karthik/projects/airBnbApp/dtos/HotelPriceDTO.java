package com.karthik.projects.airBnbApp.dtos;

import com.karthik.projects.airBnbApp.entities.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelPriceDTO {
    private Hotel hotel;
    private Double price;
}
