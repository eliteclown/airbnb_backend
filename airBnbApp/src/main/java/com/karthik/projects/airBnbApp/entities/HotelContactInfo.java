package com.karthik.projects.airBnbApp.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
public class HotelContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
