package com.karthik.projects.airBnbApp.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Embeddable
public class HotelContactInfoEntity {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
