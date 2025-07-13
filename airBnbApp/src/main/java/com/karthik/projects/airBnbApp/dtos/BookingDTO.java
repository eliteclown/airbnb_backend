package com.karthik.projects.airBnbApp.dtos;

import com.karthik.projects.airBnbApp.entities.Guest;
import com.karthik.projects.airBnbApp.entities.Hotel;
import com.karthik.projects.airBnbApp.entities.Room;
import com.karthik.projects.airBnbApp.entities.User;
import com.karthik.projects.airBnbApp.entities.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDTO> guests;
}
