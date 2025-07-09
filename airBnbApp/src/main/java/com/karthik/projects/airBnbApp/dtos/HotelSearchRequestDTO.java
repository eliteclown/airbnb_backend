package com.karthik.projects.airBnbApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchRequestDTO {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomsCount;
    private Integer page=0;
    private Integer size=10;
}
